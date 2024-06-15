package dev.smai1e.carTrader.ui.views.auctionDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.databinding.FragmentAuctionDetailsBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.utils.CallProvider
import dev.smai1e.carTrader.ui.views.MainActivity
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.composeUI.Chart
import dev.smai1e.carTrader.utils.TextWatcherProvider
import dev.smai1e.carTrader.ui.views.auctionDetails.adapter.SliderAdapter
import dev.smai1e.carTrader.utils.MoneyTextWatcher
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.ui.composeUI.CircleProgressBar
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.BidUI
import dev.smai1e.carTrader.ui.models.CarUI
import dev.smai1e.carTrader.ui.models.UserInfoUI
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.getAmount
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.loadFromUrl
import dev.smai1e.carTrader.utils.toCurrencyString
import javax.inject.Inject

class AuctionDetailsFragment : BaseFragment(), CallProvider, TextWatcherProvider {

    private var _binding: FragmentAuctionDetailsBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    private val args: AuctionDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var factory: AuctionDetailsViewModelFactory.Factory
    private val viewModel: AuctionDetailsViewModel by viewModels {
        factory.create(args.auctionId)
    }

    private val viewPagerAdapter = SliderAdapter(::onPhotoPressed)

    private lateinit var sellerInfo: UserInfoUI

    private val saveFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    viewModel.getProtocol(uri)
                }
            }
        }

    private fun requestDirectoryAccess() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        } else {
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, downloadsDirectory)
            }
        }
        saveFileLauncher.launch(intent)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuctionDetailsBinding.inflate(inflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUi()
        initViewPager()
        initConfirmDialogResultListener()
        initBidAmountTextInputListener()
        initBidAmountChangeEventListener()
        initButtonClickListeners()
        initProtocolSaveEventListener()
        initBidInsertEventListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _resultBinding = null
    }

    private fun initBidAmountTextInputListener() {
        binding.bidAmountTextInput.apply {
            setOnTextChangeListener {
                viewModel.changeBid(it.getAmount())
            }
            addTextWatcher(MoneyTextWatcher(this))
        }
    }

    private fun initBidAmountChangeEventListener() {
        viewModel.bidAmountChangeEvent.collectFlow {
            binding.bidAmountTextInput.setText(it.toString())
            binding.bidAmountTextView.text = it.toCurrencyString()
        }
    }

    private fun initButtonClickListeners() {
        binding.insertBidButton.setOnClickListener { showConfirmDialog() }
        binding.getProtocol.setOnClickListener { requestDirectoryAccess() }
        binding.plusButton.setOnClickListener { viewModel.raiseBid() }
        binding.minusButton.setOnClickListener { viewModel.lowerBid() }
        binding.sellerCardView.setOnClickListener { showSellerInfoDialog() }
    }

    private fun initProtocolSaveEventListener() {
        viewModel.protocolSaveEvent.collectFlow { result ->
            result
                .onSuccess { fileName ->
                    showSuccessSnackbar(
                        getString(
                            R.string.file_has_been_saved,
                            fileName
                        )
                    )
                }
                .onError(::showErrorSnackbar)
        }
    }

    private fun initBidInsertEventListener() {
        viewModel.bidInsertEvent.collectFlow { result ->
            result
                .onSuccess { bid ->
                    showSuccessSnackbar(
                        getString(
                            R.string.bid_reg_time,
                            bid.bidTime.convertISO8601ToFormattedDate()
                        )
                    )
                }
                .onError(::showErrorSnackbar)
        }
    }

    private fun showConfirmDialog() {
        val formattedBid = binding.bidAmountTextInput.text?.getAmount() ?: 0
        val action = AuctionDetailsFragmentDirections.actionDetailsFragmentToConfirmDialog(formattedBid)
        findTopNavController().navigate(action)
    }

    private fun showSellerInfoDialog() {
        val action = AuctionDetailsFragmentDirections.actionDetailsFragmentToSellerInfoDialog(sellerInfo)
        findTopNavController().navigate(action)
    }

    private fun onPhotoPressed(urlList: List<String>, position: Int) {
        val action = AuctionDetailsFragmentDirections.actionLotFragmentToPhotoViewFragment(
            urlList.toTypedArray(),
            position
        )
        findTopNavController().navigate(action)
    }

    private fun setProgressBar(timeState: TimeProgressState? = null) {
        binding.circleProgressBar.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    if (timeState == null) {
                        CircleProgressBar()
                    } else {
                        CircleProgressBar(timeState)
                    }
                }
            }
        }
    }

    private fun setChartView(bids: List<BidUI>) {
        binding.chartView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Chart(bids.map { it.bidAmount })
                }
            }
        }
    }

    private fun updateUi() {
        viewModel.uiState.collectFlow { uiState ->
            binding.bottomBidsBar.visibility =
                if (uiState.bottomBidsBarIsVisible) View.VISIBLE else View.GONE
            binding.bottomProtocolBar.visibility =
                if (uiState.bottomProtocolBarIsVisible) View.VISIBLE else View.GONE

            resultBinding.progressBar.visibility = if (uiState.auctionUiState is State.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.auctionPage.visibility = if (uiState.auctionUiState is State.Success) {
                bindPage(uiState.auctionUiState.data)
                View.VISIBLE
            } else {
                View.GONE
            }
            resultBinding.errorContainer.visibility = if (uiState.auctionUiState is State.Error) {
                showErrorSnackbar(uiState.auctionUiState.getErrorText(requireContext()))
                View.VISIBLE
            } else {
                View.GONE
            }

            uiState.bidsUiState
                .onSuccess(::bindBids)
                .onError(::showErrorSnackbar)

            uiState.sellerUiState
                .onSuccess(::bindSellerInfo)
                .onError(::showErrorSnackbar)

            uiState.timeUiState
                .onIdle(::setProgressBar)
                .onSuccess(::setProgressBar)
                .onError(::showErrorSnackbar)
        }
    }

    private fun bindPage(auctionInfo: AuctionUI) {
        viewPagerAdapter.submitList(auctionInfo.car.imageUrlList)
        setToolbarTitle(auctionInfo.car)
        bindAuctionInfo(auctionInfo)
    }

    private fun bindAuctionInfo(auctionInfo: AuctionUI) {
        val car = auctionInfo.car
        binding.apply {
            openDateTextView.text = auctionInfo.openDate.convertISO8601ToFormattedDate()
            closeDateTextView.text = auctionInfo.closeDate.convertISO8601ToFormattedDate()
            startBidValueTextView.text = auctionInfo.minBid.toCurrencyString()
            actualBidValueTextView.text = 0.toCurrencyString()
            bidCountValueTextView.text = 0.toString()
            statusTextView.text = getString(
                R.string.state,
                getString(auctionInfo.auctionStatus.nameId)
            )
            manufacturerDateValueTextView.text = car.manufacturerDate.toString()
            mileageValueTextView.text = getString(R.string.mileage_template, car.mileage)
            colorValueTextView.text = getString(car.color.nameId)
            gearboxValueTextView.text = getString(car.gearbox.nameId)
            driveWheelValueTextView.text = getString(car.driveWheel.nameId)
            engineValueTextView.text = getString(R.string.horsepower_template, car.horsepower)
            vinValueTextView.text = car.vin
            descriptionTextView.text = car.description.ifEmpty {
                getString(R.string.empty_description)
            }
        }
    }

    private fun bindBids(bids: List<BidUI>) {
        if (bids.isNotEmpty()) {
            val actualBid = bids.maxBy { it.bidAmount }.bidAmount
            binding.actualBidValueTextView.text = actualBid.toCurrencyString()
            binding.bidCountValueTextView.text = bids.count().toString()
            setChartView(bids)
        }
    }

    private fun bindSellerInfo(seller: UserInfoUI) {
        sellerInfo = seller
        binding.sellerNameTextView.text = getString(
            R.string.username_template,
            sellerInfo.lastName,
            sellerInfo.firstName
        )
        binding.sellerEmailTextView.text = sellerInfo.email
        binding.sellerImageView.loadFromUrl(sellerInfo.photoUrl)
        binding.callFab.setOnClickListener {
            callByPhoneNumber(sellerInfo.phone)
        }
    }

    private fun initViewPager() {
        val compositePageTransformer = CompositePageTransformer()
            .apply { addTransformer(MarginPageTransformer(40)) }
        binding.viewPagerImageSlider.apply {
            adapter = viewPagerAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
        }
    }

    private fun initConfirmDialogResultListener() {
        setFragmentResultListener(ConfirmDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(ConfirmDialog.EXTRA_KEY, false).let {
                if (it) { viewModel.insertBid() }
            }
        }
    }

    private fun setToolbarTitle(car: CarUI) {
        (activity as MainActivity).changeTitle(
            car.brand,
            "${car.model} ${car.manufacturerDate}"
        )
    }
}