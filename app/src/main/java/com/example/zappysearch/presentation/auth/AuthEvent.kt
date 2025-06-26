package com.example.zappysearch.presentation.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser

sealed class AuthEvent {
    data class LoginButtonClick(val activity : Activity) : AuthEvent()
    object SignOutButtonClick : AuthEvent()
    object GetCurrentUser : AuthEvent()
}