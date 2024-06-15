package dev.smai1e.carTrader.ui.views.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.smai1e.carTrader.domain.models.SignUpData
import dev.smai1e.carTrader.databinding.FragmentSignUpBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.appComponent
import javax.inject.Inject

class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: SignUpViewModelFactory
    private val viewModel: SignUpViewModel by viewModels {
        factory
    }

    private val args: SignUpFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountButton.setOnClickListener { onRegisterAccountButtonPressed() }

        if (savedInstanceState == null && getEmailArgument() != null) {
            binding.emailEditText.setText(getEmailArgument())
        }

        viewModel.signUpEvent.collectFlow { result ->
            result
                .onSuccess { goBack() }
                .onError(::showErrorSnackbar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onRegisterAccountButtonPressed() {
        val signUpData = SignUpData(
            email = binding.emailEditText.text.toString(),
            firstName = binding.firstNameEditText.text.toString(),
            middleName = binding.middleNameEditText.text.toString(),
            lastName = binding.lastNameEditText.text.toString(),
            phone = binding.phoneEditText.text.toString(),
            password = binding.passwordEditText.text.toString(),
        )
        viewModel.signUp(signUpData)
    }

    private fun goBack() = findNavController().popBackStack()

    private fun getEmailArgument(): String? = args.email
}