package dev.smai1e.carTrader.ui.views.newLot

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.domain.models.CarName
import dev.smai1e.carTrader.databinding.FragmentNewLotBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.utils.SpinnerAdapterProvider
import dev.smai1e.carTrader.utils.TextWatcherProvider
import dev.smai1e.carTrader.ui.views.carPicker.CarPickerFragment
import dev.smai1e.carTrader.ui.views.newLot.adapter.NewLotPhotoAdapter
import dev.smai1e.carTrader.utils.DateTimeRangedPicker
import dev.smai1e.carTrader.utils.MoneyTextWatcher
import dev.smai1e.carTrader.utils.Picker
import dev.smai1e.carTrader.utils.RealFilePath
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.getAmount
import dev.smai1e.carTrader.utils.getClipDataUris
import dev.smai1e.carTrader.utils.parcelable
import com.nareshchocha.filepickerlibrary.models.PickMediaConfig
import com.nareshchocha.filepickerlibrary.models.PickMediaType
import com.nareshchocha.filepickerlibrary.ui.FilePicker
import dev.smai1e.carTrader.utils.toIntOrGetNull
import javax.inject.Inject

class NewLotFragment : BaseFragment(), TextWatcherProvider, SpinnerAdapterProvider {

    private var _binding: FragmentNewLotBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: NewLotViewModelFactory
    private val viewModel: NewLotViewModel by viewModels {
        factory
    }

    private val mediaAdapter = NewLotPhotoAdapter(::deletePhoto)
    private val captureImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null && result.resultCode == RESULT_OK) {
                viewModel.clearUris()
                if (result.data?.data != null) {
                    RealFilePath.getPath(requireContext(), result.data?.data!!)?.let {
                        viewModel.addUri(it)
                    }
                } else {
                    result.data
                        ?.getClipDataUris()
                        ?.mapNotNull { uri -> RealFilePath.getPath(requireContext(), uri) }
                        .let { uris -> viewModel.addAllUris(uris ?: emptyList()) }
                }
            }
        }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewLotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initBidAmountTextInputListener()
        initBackEventListener()
        initButtonClickListeners()
        intiAuctionDataFlowListener()
        initTextWatchers()
        initSpinners()
        initCarPickerResultListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deletePhoto(uri: String) {
        viewModel.removeUri(uri)
    }

    private fun initCarPickerResultListener() {
        setFragmentResultListener(CarPickerFragment.REQUEST_KEY) { _, bundle ->
            bundle.parcelable<CarName>(CarPickerFragment.EXTRA_KEY)?.let {
                viewModel.setCarName(it)
            }
        }
    }

    private fun intiAuctionDataFlowListener() {
        viewModel.auctionDataFlow.collectFlow { auctionData ->
            val brand = auctionData.car.brand
            val model = auctionData.car.model
            binding.carName.text = if (!brand.isNullOrBlank() && !model.isNullOrBlank()) {
                getString(R.string.car_name_template, brand, model)
            } else {
                getString(R.string.empty_brand)
            }

            val openDate = auctionData.openDate
            val closeDAte = auctionData.closeDate
            binding.datePicker.text = if (!openDate.isNullOrBlank() && !closeDAte.isNullOrBlank()) {
                getString(
                    R.string.date_range_template,
                    openDate.convertISO8601ToFormattedDate(),
                    closeDAte.convertISO8601ToFormattedDate()
                )
            } else {
                ""
            }
        }
    }

    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mediaAdapter
        viewModel.selectedImagesFlow.collectFlow { uris ->
            mediaAdapter.submitList(uris)
        }
    }

    private fun initBidAmountTextInputListener() {
        binding.minBidAmountTextInput.apply {
            addTextWatcher(MoneyTextWatcher(this))
        }
    }

    private fun initBackEventListener() {
        viewModel.goBackEvent.collectFlow { result ->
            result
                .onSuccess {
                    showSuccessSnackbar(R.string.auction_lot_is_registered)
                    goBack()
                }
                .onError(::showErrorSnackbar)
        }
    }

    private fun initButtonClickListeners() {
        binding.apply {
            carName.setOnClickListener {
                navigateToCarPickerFragment()
            }
            datePicker.setOnClickListener {
                openDatePickerDialog()
            }
            addImageButton.setOnClickListener {
                openMediaPickerActivity()
            }
            createAuctionButton.setOnClickListener {
                viewModel.uploadAuction()
            }
        }
    }

    private fun initSpinners() {
        binding.apply {
            driveWheelSpinner.addSpinnerAdapter(driveWheelList) { index ->
                viewModel.setDriveWheel(driveWheelList[index].type)
            }
            gearboxSpinner.addSpinnerAdapter(gearboxList) { index ->
                viewModel.setGearbox(gearboxList[index].type)
            }
            colorSpinner.addSpinnerAdapter(colorList) { index ->
                viewModel.setColor(colorList[index].type)
            }
        }
    }

    private fun initTextWatchers() {
        binding.apply {
            manufacturerDateTextInput.doAfterTextChanged { }
            manufacturerDateTextInput.setOnTextChangeListener {
                viewModel.setManufacturerDate(it.toIntOrGetNull())
            }
            mileageTextInput.setOnTextChangeListener {
                viewModel.setMileage(it.toIntOrGetNull())
            }
            horsepowerTextInput.setOnTextChangeListener {
                viewModel.setHorsepower(it.toIntOrGetNull())
            }
            vinTextInput.setOnTextChangeListener {
                viewModel.setVin(it.toString())
            }
            descriptionTextInput.setOnTextChangeListener {
                viewModel.setDescription(it.toString())
            }
            minBidAmountTextInput.setOnTextChangeListener {
                viewModel.setMinBid(it.getAmount())
            }
        }
    }

    private fun openDatePickerDialog() {
        DateTimeRangedPicker.Builder()
            .pickerType(Picker.DATE_TIME)
            .childFragmentManager(childFragmentManager)
            .positiveButtonClickAction(::onPositiveButtonPressed)
            .build()
    }

    private fun openMediaPickerActivity() {
        captureImageResultLauncher.launch(
            FilePicker.Builder(requireContext())
                .pickMediaBuild(
                    PickMediaConfig(
                        mPickMediaType = PickMediaType.ImageOnly,
                        maxFiles = 10,
                        allowMultiple = true,
                    ),
                ),
        )
    }

    private fun onPositiveButtonPressed(dateRange: Pair<String, String>) {
        viewModel.setAuctionDate(dateRange.first, dateRange.second)
    }

    private fun goBack() = findTopNavController().popBackStack()

    private fun navigateToCarPickerFragment() =
        findTopNavController().navigate(R.id.action_newLotFragment_to_carPickerFragment)
}