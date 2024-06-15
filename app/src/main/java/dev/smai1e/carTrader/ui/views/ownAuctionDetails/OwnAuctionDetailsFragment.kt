package dev.smai1e.carTrader.ui.views.ownAuctionDetails

import android.app.Activity.RESULT_OK
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.databinding.FragmentOwnAuctionDetailsBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.domain.models.BidWithUser
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.ui.views.MainActivity
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.composeUI.Chart
import dev.smai1e.carTrader.ui.views.auctionDetails.adapter.SliderAdapter
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.ui.composeUI.CircleProgressBar
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.CarUI
import dev.smai1e.carTrader.ui.views.ownAuctionDetails.adapter.BidsAdapter
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.toCurrencyString
import javax.inject.Inject

class OwnAuctionDetailsFragment : BaseFragment() {

    private var _binding: FragmentOwnAuctionDetailsBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    private val args: OwnAuctionDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var factory: OwnAuctionDetailsViewModelFactory.Factory
    private val viewModel: OwnAuctionDetailsViewModel by viewModels {
        factory.create(args.auctionId)
    }

    private val viewPagerAdapter = SliderAdapter(::onPhotoPressed)
    private val bidsAdapter = BidsAdapter()

    private val saveFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
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
            val downloadsDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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
        _binding = FragmentOwnAuctionDetailsBinding.inflate(inflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        initViewPager()
        initRecyclerView()
        initProtocolListener()
        binding.getProtocol.setOnClickListener { requestDirectoryAccess() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _resultBinding = null
    }

    private fun onPhotoPressed(urlList: List<String>, position: Int) {
        val action =
            OwnAuctionDetailsFragmentDirections.actionOwnAuctionDetailsFragmentToPhotoViewFragment(
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

    private fun initProtocolListener() {
        viewModel.protocolStateEvent.collectFlow { result ->
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

    private fun setChartView(bids: List<BidWithUser>) {
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
        }
    }

    private fun bindBids(bids: List<BidWithUser>) {
        if (bids.isNotEmpty()) {
            val actualBid = bids.maxBy { it.bidAmount }.bidAmount
            binding.actualBidValueTextView.text = actualBid.toCurrencyString()
            binding.bidCountValueTextView.text = bids.count().toString()
            setChartView(bids)
            bidsAdapter.submitList(bids)
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

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true)
        binding.bidsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = bidsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    linearLayoutManager.orientation
                )
            )
        }
    }

    private fun setToolbarTitle(car: CarUI) {
        (activity as MainActivity).changeTitle(
            car.brand,
            "${car.model} ${car.manufacturerDate}"
        )
    }
}