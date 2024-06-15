package dev.smai1e.carTrader.ui.views.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.FragmentSignInBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.appComponent
import javax.inject.Inject

class SignInFragment : BaseFragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: SignInViewModelFactory
    private val viewModel: SignInViewModel by viewModels {
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
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener { onSignInButtonPressed() }
        binding.signUpButton.setOnClickListener { onSignUpButtonPressed() }
        viewModel.navigateToTabsFragmentEvent.collectFlow { result ->
            result
                .onSuccess { navigateToTabsFragment() }
                .onError(::showErrorSnackbar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSignInButtonPressed() {
        viewModel.signIn(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }

    private fun onSignUpButtonPressed() {
        val email = binding.emailEditText.text.toString()
        val emailArg = email.ifBlank { null }

        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(emailArg)
        findNavController().navigate(direction)
    }

    private fun navigateToTabsFragment() =
        findNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
}