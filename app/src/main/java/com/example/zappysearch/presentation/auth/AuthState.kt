package com.example.zappysearch.presentation.auth

import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val currentUser : FirebaseUser? = null
)