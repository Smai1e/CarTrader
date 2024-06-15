package dev.smai1e.carTrader.ui.views.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.smai1e.carTrader.ui.models.NavParams
import dev.smai1e.carTrader.domain.models.UserInfo
import dev.smai1e.carTrader.databinding.FragmentProfileBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.ui.views.profile.adapter.ButtonAdapter
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.convertToFormattedPhoneNumber
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.loadFromUrl
import dev.smai1e.carTrader.utils.toCurrencyString
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    @Inject
    lateinit var factory: ProfileViewModelFactory
    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    private val buttonsAdapter = ButtonAdapter(::navigate)

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initButtonClickListener()
        updateUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _resultBinding = null
    }

    private fun initButtonClickListener() {
        resultBinding.tryAgainButton.setOnClickListener {
            viewModel.getUserInfo()
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        buttonsAdapter.buttonsList = getProfileButtonList()
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = buttonsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    linearLayoutManager.orientation
                )
            )
        }
    }

    private fun updateUi() {
        viewModel.uiState.collectFlow { uiState ->
            resultBinding.progressBar.visibility = if (uiState is State.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.profilePage.visibility = if (uiState is State.Success) {
                bindUserInfo(uiState.data)
                View.VISIBLE
            } else {
                View.GONE
            }

            resultBinding.errorContainer.visibility = if (uiState is State.Error) {
                showErrorSnackbar(uiState.getErrorText(requireContext()))
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun bindUserInfo(userInfo: UserInfo) {
        binding.apply {
            firstNameTextView.text = userInfo.firstName
            middleNameTextView.text = userInfo.middleName
            lastNameTextView.text = userInfo.lastName
            phoneNumberValueTextView.text = userInfo.phone.convertToFormattedPhoneNumber()
            emailValueTextView.text = userInfo.email
            registrationDateValueTextView.text = userInfo.registrationDate.convertISO8601ToFormattedDate()
            avatarPhoto.loadFromUrl(userInfo.photoUrl)
            bidCountTextview.text = userInfo.bidsCount.toString()
            auctionCountTextview.text = userInfo.auctionsCount.toString()
            moneyTextview.text = userInfo.money.toCurrencyString()
        }
    }

    private fun navigate(navParams: NavParams) =
        findTopNavController().navigate(
            navParams.destinationId,
            navParams.args,
            navParams.navOptions
        )
}