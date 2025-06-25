package com.example.zappysearch.domain.repository

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login() : FirebaseUser?

    suspend fun signOut() : Unit

    fun getCurrentUser() : FirebaseUser?
}