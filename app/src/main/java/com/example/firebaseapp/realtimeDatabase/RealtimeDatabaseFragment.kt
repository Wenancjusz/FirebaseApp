package com.example.firebaseapp.realtimeDatabase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.FragmentRealtimeDatabaseBinding

class RealtimeDatabaseFragment : Fragment(), NoteAdapterClickListener {
    private var _binding: FragmentRealtimeDatabaseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RealtimeDBViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRealtimeDatabaseBinding.inflate(inflater, container, false)

        viewModel.configureDatabase(getString(R.string.database_reference))

        viewModel.downloadData {
            refreshRecyclerView()
        }

        if (!requireArguments().isEmpty) {
            if (findNavController().backQueue.size > 1) {
                repeat(2) {
                    findNavController().backQueue.removeLast()
                }
            }
            val args = RealtimeDatabaseFragmentArgs.fromBundle(arguments ?: Bundle.EMPTY)
            viewModel.saveData(args.noteTitle, args.noteText, args.previousTitle) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                } else {
                    requireArguments().clear()
                }
                refreshRecyclerView()
            }
        }

        binding.addButton.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_realtimeDatabaseFragment_to_newDataRealtimeDBFragment)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = NoteAdapter(viewModel.getItems(), false, requireContext(), this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNoteClicked(item: RealtimeDBDataClass) {
        val action =
            RealtimeDatabaseFragmentDirections.actionRealtimeDatabaseFragmentToNewDataRealtimeDBFragment(
                item.noteTitle,
                item.text, ""
            )

        NavHostFragment.findNavController(this).navigate(action)
    }

    override fun onDownloadClicked(noteTitle: String) {}

    override fun onDeleteClicked(position: Int) {
        viewModel.deleteData(position) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                refreshRecyclerView()
            }
        }
    }

    private fun refreshRecyclerView() {
        binding.noDataInDatabase.visibility = if (viewModel.getItems().isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter =
            NoteAdapter(viewModel.getItems(), false, requireContext(), this)
    }
}