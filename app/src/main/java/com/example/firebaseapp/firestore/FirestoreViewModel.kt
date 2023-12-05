package com.example.firebaseapp.firestore

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.example.firebaseapp.R

class FirestoreViewModel : ViewModel() {
    private var items: MutableList<FirestoreDataClass> = mutableListOf()
    private val database = FirebaseFirestore.getInstance()

    fun getItems(): List<FirestoreDataClass> { return items }

    fun downloadData(callback: (String?) -> Unit) {
        database.collection("Colors")
            .get()
            .addOnSuccessListener { result ->
                items = result.documents.map { document ->
                    mapDocumentToFirestoreDataClass(document)
                }.toMutableList()
                callback(null)
            }
            .addOnFailureListener { exception ->
                callback(exception.message)
            }
    }

    fun saveData(red: Int, green: Int, blue: Int, callback: (String?) -> Unit) {
        val hexCode = calculateHexCode(red, green, blue)
        val data = hashMapOf(
            "hexCode" to hexCode,
            "redValue" to red,
            "greenValue" to green,
            "blueValue" to blue
        )

        database.collection("Colors")
            .add(data)
            .addOnSuccessListener { documentReference ->
                items.add(FirestoreDataClass(hexCode, red, green, blue, documentReference.id))
                callback(null)
            }
            .addOnFailureListener { exception ->
                callback(exception.message)
            }
    }

    fun deleteData(position: Int, callback: (String?) -> Unit) {
        database.collection("Colors").document(items[position].id)
            .delete()
            .addOnSuccessListener {
                items.removeAt(position)
                callback(null)
            }
            .addOnFailureListener { e ->
                callback(e.message)
            }
    }

    private fun mapDocumentToFirestoreDataClass(document: DocumentSnapshot?): FirestoreDataClass {
        val hexCode = document?.getString("hexCode") ?: ""
        val red = document?.getLong("redValue")?.toInt() ?: 0
        val green = document?.getLong("greenValue")?.toInt() ?: 0
        val blue = document?.getLong("blueValue")?.toInt() ?: 0

        return FirestoreDataClass(hexCode, red, green, blue, document?.id ?: "")
    }

    private fun calculateHexCode(r: Int, g: Int, b: Int): String {
        return "#${r.toHexString()}${g.toHexString()}${b.toHexString()}"
    }

    fun Int.toHexString(): String {
        return Integer.toHexString(this).uppercase().padStart(2, '0')
    }
}
