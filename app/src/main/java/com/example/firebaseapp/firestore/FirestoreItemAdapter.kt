package com.example.firebaseapp.firestore

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.FirestoreItemAdapterBinding

class FirestoreItemAdapter(
    private val items: List<FirestoreDataClass>,
    private val callback: FirestoreAdapterClickListener
) : RecyclerView.Adapter<FirestoreItemAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: FirestoreItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val redTV = binding.colorRedValue
        val blueTV = binding.colorBlueValue
        val greenTV = binding.colorGreenValue
        val hexCodeTV = binding.colorHexCode
        val red = binding.redValue
        val blue = binding.blueValue
        val green = binding.greenValue
        var colorCode = binding.hexCode
        val background = binding.backgroundConstraintView
        val touchableArea = binding.touchableArea
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FirestoreItemAdapterBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val textColor = getContrastColor(items[position].hexCode)

        holder.redTV.setTextColor(textColor)
        holder.red.setTextColor(textColor)

        holder.greenTV.setTextColor(textColor)
        holder.green.setTextColor(textColor)

        holder.blueTV.setTextColor(textColor)
        holder.blue.setTextColor(textColor)

        holder.colorCode.setTextColor(textColor)
        holder.hexCodeTV.setTextColor(textColor)

        holder.binding.deleteButton.setImageResource(
            if (textColor == Color.BLACK) {
                R.drawable.baseline_delete_black
            } else {
                R.drawable.baseline_delete_white
            }
        )

        holder.red.text = items[position].redValue.toString()
        holder.green.text = items[position].greenValue.toString()
        holder.blue.text = items[position].blueValue.toString()
        holder.colorCode.text = items[position].hexCode
        holder.background.setBackgroundColor(Color.parseColor(items[position].hexCode))

        holder.touchableArea.setOnClickListener {
            callback.onEditClick(position)
        }

        holder.binding.deleteButton.setOnClickListener {
            callback.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getContrastColor(hexColor: String): Int {
        val color = Color.parseColor(hexColor)
        val luminance = (
                0.299 * Color.red(color) +
                        0.587 * Color.green(color) +
                        0.114 * Color.blue(color)
                ) / 255.0

        return if (luminance > 0.5) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }
}
