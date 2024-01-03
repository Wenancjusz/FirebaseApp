package com.example.firebaseapp.firestore

interface FirestoreAdapterClickListener {
    fun onDeleteClick(itemPosition: Int)
    fun onEditClick(itemPosition: Int)
}