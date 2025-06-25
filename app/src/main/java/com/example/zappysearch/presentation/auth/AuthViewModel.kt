package com.example.zappysearch.presentation.auth

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
            AuthEvent.LoginButtonClick -> {
                login()
            }
            AuthEvent.SignOutButtonClick -> {
                logout()
            }
        }


    }

    private fun login(){
        viewModelScope.launch {
            val currentUser = authRepository.login()
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