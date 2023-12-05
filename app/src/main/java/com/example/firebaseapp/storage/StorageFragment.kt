package com.example.firebaseapp.storage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebaseapp.databinding.FragmentStorageBinding
import com.example.firebaseapp.realtimeDatabase.NoteAdapter
import com.example.firebaseapp.realtimeDatabase.NoteAdapterClickListener
import com.example.firebaseapp.realtimeDatabase.RealtimeDBDataClass
import com.example.firebaseapp.R

class StorageFragment : Fragment(), NoteAdapterClickListener {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StorageViewModel by viewModels()
    private val chooseImageToUpload =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)

        binding.uploadBTN.setOnClickListener {
            viewModel.uploadFile(requireContext()) { message ->
                handleMessage(
                    if (message == null) getString(R.string.upload_success)
                    else getString(R.string.error) + message)
            }
        }

        binding.storageImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            chooseImageToUpload.launch(intent)
        }

        viewModel.getAllFilesNames() { fileNames ->
            val fileNamesData = mutableListOf<RealtimeDBDataClass>()
            for (fileName in fileNames) {
                fileNamesData.add(RealtimeDBDataClass(fileName, null))
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = NoteAdapter(fileNamesData, true, requireContext(), this)
        }

        return binding.root
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val fileUri: Uri = data.data!!
            binding.storageImageView.setImageURI(fileUri)
            viewModel.chosenImageUri = fileUri
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDownloadClicked(noteTitle: String) {
        viewModel.downloadFile(noteTitle) { isSuccessful, downloadUri, message ->
            handleMessage(getString(R.string.download_success))
            if (isSuccessful) {
                Glide.with(requireContext())
                    .load(downloadUri)
                    .into(binding.storageImageView)
            } else {
                Toast.makeText(context, getString(R.string.error) + message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNoteClicked(item: RealtimeDBDataClass) {}
    override fun onDeleteClicked(position: Int) {}
}