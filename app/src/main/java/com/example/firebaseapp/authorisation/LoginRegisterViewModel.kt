package com.example.firebaseapp.authorisation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.firebaseapp.R

class LoginRegisterViewModel(private val activity: Activity) : ViewModel() {
    lateinit var googleApiClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()

    init {
        configureGoogleSignIn()
    }

    fun validatePassword(password: String): Boolean {
        val regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\",<.>/?]).{6,}\$".toRegex()
        return password.matches(regexPattern)
    }

    fun validateEmail(email: String): Boolean {
        val regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return email.matches(regexPattern)
    }

    fun signInWithFirebase(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            callback(it.isSuccessful, it.exception?.message)
        }
    }

    fun signUpWithFirebase(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInWithFirebase(email, password) { succeeded, errorMessage ->
                    callback(succeeded, errorMessage)
                }
            }
            callback(false, it.exception?.message)
        }
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.baseContext.resources.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleApiClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signOut() {
        if (auth.currentUser?.providerData?.last()?.providerId == GoogleAuthProvider.PROVIDER_ID) {
            googleApiClient.revokeAccess()
        }
        auth.signOut()
    }

    fun firebaseAuthWithGoogle(idToken: String, callback: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            callback(
                task.isSuccessful, if (task.isSuccessful) {
                    null
                } else {
                    activity.baseContext.resources.getString(R.string.auth_failed)
                }
            )
        }
    }
}

class LoginRegisterViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginRegisterViewModel::class.java)) {
            return LoginRegisterViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}