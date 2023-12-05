package com.example.firebaseapp.realtimeDatabase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.firebaseapp.databinding.FragmentNewDataRealtimeDBBinding
import com.example.firebaseapp.R

class NewDataRealtimeDBFragment : Fragment() {
    private var _binding: FragmentNewDataRealtimeDBBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewDataRealtimeDBBinding.inflate(inflater, container, false)
        var noteTitle: String? = null
        var noteText: String? = null
        var previousTitle: String? = null

        if (!requireArguments().isEmpty) {
            val args = NewDataRealtimeDBFragmentArgs.fromBundle(requireArguments())
            noteTitle = args.noteTitle
            noteText = args.noteText
            previousTitle = args.noteTitle
        }

        binding.noteTitleET.setText(noteTitle)
        binding.editTextText3.setText(noteText)

        binding.saveBTN.setOnClickListener {
            if (binding.noteTitleET.text.toString().isNotEmpty()) {
                val action = NewDataRealtimeDBFragmentDirections
                    .actionNewDataRealtimeDBFragmentToRealtimeDatabaseFragment(
                        binding.noteTitleET.text.toString(),
                        binding.editTextText3.text.toString(),
                        previousTitle
                    )

                NavHostFragment.findNavController(this).navigate(action)
            } else {
                Toast.makeText(context, R.string.enter_note_title, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}