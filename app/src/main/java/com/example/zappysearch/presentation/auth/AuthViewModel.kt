package com.example.zappysearch.presentation.auth

import android.app.Activity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zappysearch.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = mutableStateOf(AuthState())
    val state  : State<AuthState> = _state
    init {
        onAuthEvent(AuthEvent.GetCurrentUser)
    }
    fun onAuthEvent(event : AuthEvent){
        when(event){
            AuthEvent.GetCurrentUser -> {
              val currentUser =   authRepository.getCurrentUser()
                _state.value = state.value.copy(
                    currentUser = currentUser
                )
            }
            is AuthEvent.LoginButtonClick -> {
                login(event.activity)
            }
            AuthEvent.SignOutButtonClick -> {
                logout()
            }
        }


    }

    private fun login(activity : Activity){
        viewModelScope.launch {
            val currentUser = authRepository.login(activity = activity)
            _state.value = state.value.copy(
                currentUser = currentUser
            )
        }
    }

    private fun logout(){
        viewModelScope.launch{
            authRepository.signOut()
            _state.value = state.value.copy(
                currentUser = null
            )
        }
    }

}