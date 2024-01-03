package com.example.firebaseapp.realtimeDatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.NoteAdapterBinding

class NoteAdapter(
    private val items: List<RealtimeDBDataClass>,
    private val isStorage: Boolean,
    private val callback: NoteAdapterClickListener
) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: NoteAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTitle = binding.noteTitle
        val actionBTN = binding.actionBTN
        val view = binding.view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteAdapterBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noteTitle.text = items[position].noteTitle
        val drawable = if (isStorage) {
            R.drawable.baseline_download_24
        } else {
            R.drawable.baseline_delete_white
        }
        holder.actionBTN.setImageResource(drawable)
        holder.actionBTN.setOnClickListener {
            if (isStorage) {
                callback.onDownloadClicked(holder.noteTitle.text.toString())
            } else {
                callback.onDeleteClicked(position)
            }
        }

        holder.view.setOnClickListener {
            callback.onNoteClicked(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}