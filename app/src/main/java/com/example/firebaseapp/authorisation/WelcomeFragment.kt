package com.example.firebaseapp.authorisation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.FragmentWelcomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginRegisterViewModel by viewModels {
        LoginRegisterViewModelFactory(requireActivity())
    }
    private val googleSignInActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionSignInFragmentToLoginRegisterFragment(true)
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.signUpButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionSignInFragmentToLoginRegisterFragment(false)
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.signInWithGoogleButton.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
    }

    private fun signInWithGoogle() {
        val signInIntent = viewModel.googleApiClient.signInIntent
        googleSignInActivityResult.launch(signInIntent)
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == android.app.Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    viewModel.firebaseAuthWithGoogle(idToken) { isSuccessful, errorMessage ->
                        if (isSuccessful) {
                            NavHostFragment.findNavController(this).navigate(
                                R.id.action_signInFragment_to_homeFragment
                            )
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}