package com.example.zappysearch.domain.repository

import android.app.Activity
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(activity : Activity) : FirebaseUser?

    suspend fun signOut() : Unit

    fun getCurrentUser() : FirebaseUser?
}