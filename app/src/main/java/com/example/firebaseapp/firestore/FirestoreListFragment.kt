package com.example.firebaseapp.firestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.FragmentFirestoreListBinding

class FirestoreListFragment : Fragment(), FirestoreAdapterClickListener, OnDialogDismissedListener {
    private var _binding: FragmentFirestoreListBinding? = null
    private lateinit var dialog: AddFirestoreDataFragment
    private val binding get() = _binding!!
    private val viewModel: FirestoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirestoreListBinding.inflate(inflater, container, false)
       // val navBackStackEntry = navController.getBackStackEntry(R.id.firestoreListFragment)
/*
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME &&
                navBackStackEntry.savedStateHandle.contains("red")
            ) {
                val savedState = navController.currentBackStackEntry?.savedStateHandle
                val red = savedState?.get<Int>("red")
                val green = savedState?.get<Int>("green")
                val blue = savedState?.get<Int>("blue")
                if (red != null && green != null && blue != null) {
                    viewModel.saveData(red, green, blue) { errorMessage ->
                        handleResponse(errorMessage)
                    }
                    navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("red")
                    navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("green")
                    navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("blue")
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        )
*/
        viewModel.downloadData { errorMessage ->
            handleResponse(errorMessage)
        }

        binding.addButton.setOnClickListener {
            showDialog(null)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleError(message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun refreshRecyclerView() {
        if (viewModel.getItems().isEmpty()) {
            binding.noDataInFirestore.visibility = View.VISIBLE
        } else {
            binding.noDataInFirestore.visibility = View.GONE
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter =
                FirestoreItemAdapter(viewModel.getItems(), this)
        }
    }

    private fun handleResponse(errorMessage: String?) {
        if (errorMessage != null) {
            handleError(getString(R.string.error) + errorMessage)
        } else {
            refreshRecyclerView()
        }
    }

    override fun onDeleteClick(itemPosition: Int) {
        viewModel.deleteData(itemPosition) { errorMessage ->
            handleResponse(errorMessage)
        }
    }

    override fun onEditClick(itemPosition: Int) {
        val item = viewModel.getItems()[itemPosition]
        showDialog(item)
    }

    private fun showDialog(item: FirestoreDataClass?) {
        dialog = if (item == null) AddFirestoreDataFragment(dismissedListener = this)
                 else AddFirestoreDataFragment(item.redValue, item.greenValue, item.blueValue, item.id, this)

        binding.view.visibility = View.VISIBLE
        binding.addButton.visibility = View.GONE
        dialog.show(childFragmentManager, null)
    }

    override fun onDialogDismissed() {
        binding.view.visibility = View.GONE
        binding.addButton.visibility = View.VISIBLE
        val navController = findNavController()

        val savedState = navController.currentBackStackEntry?.savedStateHandle
        val red = savedState?.get<Int>("red")
        val green = savedState?.get<Int>("green")
        val blue = savedState?.get<Int>("blue")
        val id = savedState?.get<String>("id")

        if (red != null && green != null && blue != null) {
            viewModel.saveData(red, green, blue, id) { errorMessage ->
                handleResponse(errorMessage)
            }
            navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("red")
            navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("green")
            navController.currentBackStackEntry!!.savedStateHandle.remove<Int>("blue")
            navController.currentBackStackEntry!!.savedStateHandle.remove<String>("id")
        }
    }
}
