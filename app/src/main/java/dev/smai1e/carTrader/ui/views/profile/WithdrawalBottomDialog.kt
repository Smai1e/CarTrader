package dev.smai1e.carTrader.ui.views.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.WithdrawalBottomDialogBinding
import dev.smai1e.carTrader.ui.views.BaseDialogFragment
import dev.smai1e.carTrader.utils.MoneyTextWatcher
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.getAmount
import javax.inject.Inject

class WithdrawalBottomDialog : BaseDialogFragment() {

    private var _binding: WithdrawalBottomDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: WalletBottomDialogViewModelFactory
    private val viewModel: WalletBottomDialogViewModel by viewModels {
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
        _binding = WithdrawalBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMoneyTextInputListener()
        initButtonClickListener()
        initWithdrawResultEventListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMoneyTextInputListener() {
        binding.moneyTextInput.apply{
            addTextChangedListener(MoneyTextWatcher(this))
        }
    }

    private fun initButtonClickListener() {
        binding.confirmButton.setOnClickListener {
            viewModel.withdraw(
                money = binding.moneyTextInput.text.getAmount(),
                number = binding.numberTextInput.unmaskedText
            )
        }
    }

    private fun initWithdrawResultEventListener() {
        viewModel.uiState.collectFlow { result ->
            result
                .onSuccess {
                    showSuccessSnackbar(R.string.operation_was_completed_successfully)
                    findNavController().popBackStack()
                }
                .onError(::showErrorSnackbar)
        }
    }
}