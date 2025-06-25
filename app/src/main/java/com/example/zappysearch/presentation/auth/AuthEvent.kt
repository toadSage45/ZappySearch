package com.example.zappysearch.presentation.auth

import com.google.firebase.auth.FirebaseUser

sealed class AuthEvent {
    object LoginButtonClick : AuthEvent()
    object SignOutButtonClick : AuthEvent()
    object GetCurrentUser : AuthEvent()
}