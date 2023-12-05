package com.example.firebaseapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseapp.R
import com.example.firebaseapp.authorisation.LoginRegisterViewModel
import com.example.firebaseapp.authorisation.LoginRegisterViewModelFactory
import com.example.firebaseapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ItemAdapterClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginRegisterViewModel by viewModels {
        LoginRegisterViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
            NavHostFragment.findNavController(this).navigate(
                R.id.action_homeFragment_to_signInFragment
            )
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = ListItemAdapter(
            resources.getStringArray(R.array.firebase_functions).toList(),
            requireContext(),
            this
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(itemPosition: Int) {
        moveToFunction(itemPosition)
    }

    private fun moveToFunction(functionID: Int) {
        when (functionID) {
            0 -> NavHostFragment.findNavController(this).navigate(
                R.id.action_homeFragment_to_firestoreListFragment
            )

            1 -> NavHostFragment.findNavController(this).navigate(
                R.id.action_homeFragment_to_realtimeDatabaseFragment
            )

            2 -> NavHostFragment.findNavController(this).navigate(
                R.id.action_homeFragment_to_storageFragment
            )

            3 -> NavHostFragment.findNavController(this).navigate(
                R.id.action_homeFragment_to_remoteConfigFragment
            )
        }
    }
}
