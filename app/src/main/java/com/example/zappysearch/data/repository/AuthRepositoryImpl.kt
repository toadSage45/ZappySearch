package com.example.zappysearch.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zappysearch.domain.repository.AuthRepository
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl
@Inject
constructor(private val auth : FirebaseAuth,
            private val credentialManager : CredentialManager,
            private val request : GetCredentialRequest,
            @ApplicationContext private val context : Context
    ) : AuthRepository {

    override suspend fun login(activity : Activity): FirebaseUser? {
        try{
            val result = credentialManager.getCredential(context = activity , request = request);
            return  handleSignIn(result.credential);
        }catch(e: GetCredentialException){
            Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            return null
        }
    }



    private suspend fun handleSignIn(credential : Credential) : FirebaseUser?{
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            return firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {

            Log.w(TAG, "Credential is not of type Google ID!")
            return null
        }
    }


    private suspend fun firebaseAuthWithGoogle(idToken: String) : FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        try{
            auth.signInWithCredential(credential).await()
            Log.d(TAG, "signInWithCredential:success")
            return auth.currentUser
        }catch (e : Exception){
            Log.w(TAG, "signInWithCredential:failure", e)
            return null
        }


    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }



    override suspend fun signOut() {
        auth.signOut()
        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        }catch (e: ClearCredentialException){
            Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
        }
    }


    companion object {
        private const val TAG = "GoogleActivity"
    }
}