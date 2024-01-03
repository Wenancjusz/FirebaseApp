package com.example.firebaseapp.firestore

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.firebaseapp.databinding.FragmentAddFirestoreDataBinding
import com.example.firebaseapp.R

class AddFirestoreDataFragment(private var red: Int = -1,
                               private var green: Int = -1,
                               private var blue: Int = -1,
                               private val id: String? = null,
                               private val dismissedListener: OnDialogDismissedListener) : DialogFragment() {
    private lateinit var binding: FragmentAddFirestoreDataBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentAddFirestoreDataBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = true

        builder.setView(binding.root)

        if (red != -1 && green != -1 && blue != -1) {
            binding.redValueET.setText(red.toString())
            binding.greenValueET.setText(green.toString())
            binding.blueValueET.setText(blue.toString())
            binding.colorView.background = Color.rgb(red, green, blue).toDrawable()
        } else {
            binding.colorView.background = Color.BLACK.toDrawable()
        }

        val text = if (red != -1) getString(R.string.edit) else getString(R.string.save)
        binding.addColorTV.text = "$text color"

        binding.redValueET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    val isColorValidated = validateColor(s.toString().toInt())
                    red = if (isColorValidated) s.toString().toInt() else 0
                    binding.redColorError.visibility =
                        if (isColorValidated) View.GONE else View.VISIBLE
                } else {
                    red = 0
                    binding.redColorError.visibility = View.VISIBLE
                }
                binding.colorView.background = Color.rgb(red, green, blue).toDrawable()
            }
        })

        binding.greenValueET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    val isColorValidated = validateColor(s.toString().toInt())
                    green = if (isColorValidated) s.toString().toInt() else 0
                    binding.greenColorError.visibility =
                        if (isColorValidated) View.GONE else View.VISIBLE
                } else {
                    green = 0
                    binding.greenColorError.visibility = View.VISIBLE
                }
                binding.colorView.background = Color.rgb(red, green, blue).toDrawable()
            }
        })

        binding.blueValueET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    val isColorValidated = validateColor(s.toString().toInt())
                    blue = if (isColorValidated) s.toString().toInt() else 0
                    binding.blueColorError.visibility =
                        if (isColorValidated) View.GONE else View.VISIBLE
                } else {
                    blue = 0
                    binding.blueColorError.visibility = View.VISIBLE
                }
                binding.colorView.background = Color.rgb(red, green, blue).toDrawable()
            }
        })

        binding.addColorBTN.setOnClickListener {
            val redError = !validateColor(red)
            val greenError = !validateColor(green)
            val blueError = !validateColor(blue)

            binding.redColorError.visibility = if (redError) View.VISIBLE else View.GONE
            binding.greenColorError.visibility = if (greenError) View.VISIBLE else View.GONE
            binding.blueColorError.visibility = if (blueError) View.VISIBLE else View.GONE

            val isThereError = redError || greenError || blueError

            if (!isThereError) {
                val navController = findNavController()
                val savedState = navController.currentBackStackEntry?.savedStateHandle
                savedState?.set("red", red)
                savedState?.set("green", green)
                savedState?.set("blue", blue)
                savedState?.set("id", id)
                this.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.gravity = Gravity.CENTER
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissedListener.onDialogDismissed()
    }

    private fun validateColor(color: Int): Boolean {
        return color in 0..255
    }
}
