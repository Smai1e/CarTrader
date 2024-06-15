package dev.smai1e.carTrader.ui.views.filterPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.domain.models.CarName
import dev.smai1e.carTrader.databinding.FragmentFilterBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.utils.SpinnerAdapterProvider
import dev.smai1e.carTrader.utils.TextWatcherProvider
import dev.smai1e.carTrader.ui.views.auctionsList.AuctionsListViewModel
import dev.smai1e.carTrader.ui.views.auctionsList.AuctionsListViewModelFactory
import dev.smai1e.carTrader.ui.views.carPicker.CarPickerFragment
import dev.smai1e.carTrader.utils.DateTimeRangedPicker
import dev.smai1e.carTrader.utils.MoneyTextWatcher
import dev.smai1e.carTrader.utils.Picker
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.getAmount
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.parcelable
import dev.smai1e.carTrader.utils.toIntOrGetNull
import javax.inject.Inject

class FilterFragment : BaseFragment(), TextWatcherProvider, SpinnerAdapterProvider {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: AuctionsListViewModelFactory
    private val viewModel: AuctionsListViewModel by activityViewModels {
        factory
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarMenu()
        initSpinners()
        initTextWatchers()
        initCarPickerResultListener()
        initCarNameResultEventListener()
        initButtonListener()
        initBidAmountTextInputListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_filter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.reset -> {
                        viewModel.reset()
                        goBack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initCarPickerResultListener() {
        setFragmentResultListener(CarPickerFragment.REQUEST_KEY) { _, bundle ->
            bundle.parcelable<CarName>(CarPickerFragment.EXTRA_KEY)?.let {
                viewModel.setCarName(it)
            }
        }
    }

    private fun initCarNameResultEventListener() {
        viewModel.searchParamsFlow.collectFlow { searchParams ->
            binding.carName.text = if (searchParams.brand != null) {
                getString(R.string.car_name_template, searchParams.brand, searchParams.model ?: "")
            } else {
                getString(R.string.empty_brand)
            }
        }
    }

    private fun initTextWatchers() {
        binding.apply {
            minBidAmountStartTextInput.setOnTextChangeListener {
                viewModel.setMinBidStart(it.getAmount())
            }
            minBidAmountEndTextInput.setOnTextChangeListener {
                viewModel.setMinBidEnd(it.getAmount())
            }
            horsepowerStartTextInput.setOnTextChangeListener {
                viewModel.setHorsepowerStart(it.toIntOrGetNull())
            }
            horsepowerEndTextInput.setOnTextChangeListener {
                viewModel.setHorsepowerEnd(it.toIntOrGetNull())
            }
            manufacturerDateStart.setOnTextChangeListener {
                viewModel.setManufacturerDateStart(it.toIntOrGetNull())
            }
            manufacturerDateEnd.setOnTextChangeListener {
                viewModel.setManufacturerDateEnd(it.toIntOrGetNull())
            }
            mileageStart.setOnTextChangeListener {
                viewModel.setMileageStart(it.toIntOrGetNull())
            }
            mileageEnd.setOnTextChangeListener {
                viewModel.setMileageEnd(it.toIntOrGetNull())
            }
        }
    }

    private fun initButtonListener() {
        binding.apply {
            carName.setOnClickListener {
                navigateToCarPickerFragment()
            }
            datePicker.setOnClickListener {
                openDatePickerDialog()
            }
            searchButton.setOnClickListener {
                viewModel.getAuctions()
                goBack()
            }
        }
    }

    private fun initBidAmountTextInputListener() {
        binding.minBidAmountStartTextInput.apply {
            addTextWatcher(MoneyTextWatcher(this))
            setText(0.toString())
        }
        binding.minBidAmountEndTextInput.apply {
            addTextWatcher(MoneyTextWatcher(this))
            setText(10_000_000.toString())
        }
    }

    private fun initSpinners() {
        binding.apply {
            gearboxSpinner.addSpinnerAdapter(gearboxList) { index ->
                viewModel.setGearbox(gearboxList[index].type)
            }
            driveWheelSpinner.addSpinnerAdapter(driveWheelList) { index ->
                viewModel.setDriveWheel(driveWheelList[index].type)
            }
            colorSpinner.addSpinnerAdapter(colorList) { index ->
                viewModel.setColor(colorList[index].type)
            }
        }
    }

    private fun openDatePickerDialog() {
        DateTimeRangedPicker.Builder()
            .pickerType(Picker.DATE)
            .childFragmentManager(childFragmentManager)
            .positiveButtonClickAction(::onPositiveButtonClicked)
            .build()
    }

    private fun onPositiveButtonClicked(dateRange: Pair<String, String>) {
        viewModel.setAuctionDate(dateRange.first, dateRange.second)
        binding.datePicker.text = getString(
            R.string.date_range_template,
            dateRange.first.convertISO8601ToFormattedDate(),
            dateRange.second.convertISO8601ToFormattedDate()
        )
    }

    private fun navigateToCarPickerFragment() =
        findTopNavController().navigate(R.id.action_filterFragment_to_carPickerFragment)

    private fun goBack() = findTopNavController().popBackStack()
}