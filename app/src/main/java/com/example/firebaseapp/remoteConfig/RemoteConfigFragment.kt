package com.example.firebaseapp.remoteConfig

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebaseapp.databinding.FragmentRemoteConfigBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.example.firebaseapp.R

class RemoteConfigFragment : Fragment() {
    private var _binding: FragmentRemoteConfigBinding? = null
    private val binding get() = _binding!!
    private val TAG = "RemoteConfig"
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRemoteConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchRemoteConfigValues()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchRemoteConfigValues() {
        remoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                Log.d(TAG, getString(R.string.fetch_succeded))
                applyRemoteConfigValuesToUI()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, getString(R.string.fetch_failed, exception))
            }
    }

    private fun applyRemoteConfigValuesToUI() {
        binding.remoteTextView.text = remoteConfig.getString("remoteString")
        binding.switch1.isChecked = remoteConfig.getBoolean("remoteBoolean")
    }
}