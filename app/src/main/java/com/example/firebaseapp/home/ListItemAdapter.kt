package com.example.firebaseapp.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.databinding.ListItemAdapterBinding

class ListItemAdapter(
    private val items: List<String>,
    private val context: Context,
    private val callback: ItemAdapterClickListener
) : RecyclerView.Adapter<ListItemAdapter.MyViewHolder>() {

    private val features = listOf(
        "Cloud Firestore",
        "Realtime Database",
        "Cloud Storage",
        "Remote Config"
    )


    class MyViewHolder(val binding: ListItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageBtn = binding.imageButton
        val featureName = binding.featureName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAdapterBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imageBtn.background = context.resources.getDrawable(
            context.resources.getIdentifier(
                items[position],
                "drawable",
                context.packageName
            ),
            null
        )
        holder.featureName.text = features[position]
        holder.itemView.setOnClickListener {
            callback.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
