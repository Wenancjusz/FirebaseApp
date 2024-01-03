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
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val userChild = database.child(mainChild).child(uid)

        if (previousTitle.isNullOrBlank()) {
            userChild.child(noteTitle).setValue(noteText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        items.add(note)
                    }
                    callback(task.exception?.message)
                }
        } else {
            val updateData = mapOf(
                previousTitle to null,
                noteTitle to noteText
            )

            userChild.updateChildren(updateData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        items.removeAll { it.noteTitle == previousTitle }
                        items.add(RealtimeDBDataClass(noteTitle, noteText))
                    } else {
                        userChild.child(noteTitle).removeValue()
                    }
                    callback(task.exception?.message)
                }
        }
    }


    fun deleteData(position: Int, callback: (String?) -> Unit) {
        database.child(mainChild)
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .child(items[position].noteTitle)
            .removeValue { error, _ ->
                items.removeAt(position)
                callback(error?.message)
            }
    }
}