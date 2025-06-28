package com.example.zappysearch.presentation.screens


import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.presentation.auth.AuthEvent
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel

@Composable
fun AuthProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val authState = authViewModel.state.value
    val user = authState.currentUser
    val activity = LocalActivity.current


    LaunchedEffect(Unit) {
        authViewModel.onAuthEvent(AuthEvent.GetCurrentUser)
    }

    LaunchedEffect(user) {
        user?.let {
            chatViewModel.onChatEvent(
                ChatEvent.SetUserInfo(
                    AppUser(
                        userId = it.uid,
                        email = it.email ?: "No email",
                        name = it.displayName ?: "No Name",
                        photoUrl = it.photoUrl.toString()
                    )
                )
            )
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (user != null) {

            Text("Name: ${user.displayName ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
            Text("Email: ${user.email ?: "Not available"}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { authViewModel.onAuthEvent(AuthEvent.SignOutButtonClick) }) {
                Text("Sign Out")
            }
        } else {
            Text("You are not logged in.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                activity?.let{activity->
                    authViewModel.onAuthEvent(AuthEvent.LoginButtonClick(activity = activity))
                }

            }) {
                Text("Sign In with Google")
            }
        }


    }
}
