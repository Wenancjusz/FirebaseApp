package com.example.firebaseapp.storage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.example.firebaseapp.R

class StorageViewModel : ViewModel() {
    private val storage = FirebaseStorage.getInstance().reference
    var chosenImageUri: Uri? = null

    fun uploadFile(context: Context, callback: (String?) -> Unit) {
        chosenImageUri?.let { imageUri ->
            val fileRef = storage.child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .child(getFileName(context, imageUri) ?: "unknownFile")
            fileRef.putFile(imageUri)
                .addOnCompleteListener { task ->
                    callback(
                        if (task.isSuccessful) null
                        else task.exception?.message)
                }
        }
    }

    fun downloadFile(fileName: String, callback: (Boolean, Uri, String?) -> Unit) {
        val fileRef =
            storage.child(FirebaseAuth.getInstance().currentUser?.uid ?: "").child("${fileName}")
        fileRef.downloadUrl
            .addOnCompleteListener { task ->
                callback(
                    task.isSuccessful,
                    task.result,
                    if (task.isSuccessful) null else task.exception?.message
                )
            }
    }

    fun getAllFilesNames(callback: (List<String>) -> Unit) {
        val fileNames = mutableListOf<String>()
        storage.child(FirebaseAuth.getInstance().currentUser?.uid ?: "").listAll()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (item in task.result?.items!!) {
                        val fileName = item.name
                        fileNames.add(fileName)
                    }
                }
                callback(fileNames)
            }
    }

    private fun getFileName(context: Context, fileUri: Uri): String? {
        var fileName: String? = null
        if (fileUri.scheme == ContentResolver.SCHEME_CONTENT) {
            val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)

            context.contentResolver.query(fileUri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    fileName = cursor.getString(columnIndex)
                }
            }
        }

        if (fileUri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = fileUri.lastPathSegment
        }

        return fileName
    }

}