package com.example.firebaseapp.realtimeDatabase

interface NoteAdapterClickListener {
    fun onDownloadClicked(noteTitle: String)
    fun onNoteClicked(item: RealtimeDBDataClass)
    fun onDeleteClicked(position: Int)
}