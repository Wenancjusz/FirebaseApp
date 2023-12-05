package com.example.firebaseapp.authorisation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.FragmentLoginRegisterBinding

class LoginRegisterFragment : Fragment() {
    private var _binding: FragmentLoginRegisterBinding? = null
    private val binding get() = _binding!!
    private var isLoginMode: Boolean = false
    private val viewModel: LoginRegisterViewModel by viewModels {
        LoginRegisterViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginRegisterBinding.inflate(inflater, container, false)

        val args = LoginRegisterFragmentArgs.fromBundle(requireArguments())
        isLoginMode = args.isSignIn

        binding.titleTV.text = if (isLoginMode) getString(R.string.sign_in) else getString(R.string.sign_up)
        binding.actionButton.text = if (isLoginMode) getString(R.string.sign_in) else getString(R.string.sign_up)

        binding.actionButton.setOnClickListener {
            val email = binding.emailAddress.text.toString()
            val password = binding.password.text.toString()

            val isValidEmail = viewModel.validateEmail(email)
            val isValidPassword = viewModel.validatePassword(password)

            if (isValidEmail && isValidPassword) {
                if (isLoginMode) {
                    viewModel.signInWithFirebase(email, password) { succeeded, errorMessage ->
                        handleActionResult(succeeded, errorMessage)
                    }
                } else {
                    viewModel.signUpWithFirebase(email, password) { succeeded, errorMessage ->
                        handleActionResult(succeeded, errorMessage)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleActionResult(succeeded: Boolean, errorMessage: String?) {
        if (succeeded) {
            findNavController().navigate(R.id.action_loginRegisterFragment_to_homeFragment)
        } else {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
