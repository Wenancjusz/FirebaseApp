package com.example.firebaseapp.realtimeDatabase

import androidx.lifecycle.ViewModel
import com.example.firebaseapp.firestore.FirestoreDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.firebaseapp.R

class RealtimeDBViewModel : ViewModel() {
    private var items: MutableList<RealtimeDBDataClass> = mutableListOf()
    private var database = FirebaseDatabase.getInstance().reference
    private val mainChild = "users"

    fun configureDatabase(databaseReference: String) {
        database = FirebaseDatabase.getInstance(databaseReference).reference
    }

    fun getItems(): List<RealtimeDBDataClass> { return items }

    fun downloadData(callback: (String?) -> Unit) {
        database.child(mainChild)
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    items.clear()
                    for (noteData in task.result.children) {
                        val note = RealtimeDBDataClass(
                            noteData?.key ?: "",
                            noteData?.value.toString()
                        )
                        items.add(note)
                    }
                }
                callback(task.exception?.message)
            }
    }

    fun saveData(
        noteTitle: String,
        noteText: String?,
        previousTitle: String?,
        callback: (String?) -> Unit
    ) {
        val note = RealtimeDBDataClass(noteTitle, noteText)

        if (previousTitle == null || previousTitle == "") {
            database.child(mainChild)
                .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .child(noteTitle).setValue(noteText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        items.add(note)
                    }
                    callback(task.exception?.message)
                }
        } else {
            database.child(mainChild)
                .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .child(noteTitle).setValue(noteText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        database.child(mainChild)
                            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                            .child(previousTitle)
                            .removeValue() { error, _ ->
                                if (error == null) {
                                    items.remove(RealtimeDBDataClass(previousTitle, noteText))
                                    items.add(note)
                                } else {
                                    database.child(mainChild)
                                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                        .child(noteTitle).removeValue()
                                    callback(error.message)
                                }
                            }
                    } else {
                        callback(task.exception?.message)
                    }
                }
        }
    }

    fun deleteData(position: Int, callback: (String?) -> Unit) {
        database.child(mainChild)
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .child(items[position].noteTitle)
            .removeValue() { error, _ ->
                items.removeAt(position)
                callback(if (error != null) error.message else null)
            }
    }
}