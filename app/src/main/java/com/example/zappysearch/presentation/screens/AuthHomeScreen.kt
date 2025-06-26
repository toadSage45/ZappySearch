package com.example.zappysearch.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.zappysearch.presentation.auth.AuthEvent
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.navigation.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.domain.model.Post
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthHomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    navController: NavController
) {
    val authState = authViewModel.state.value
    val chatState = chatViewModel.chatState.value
    val user = authState.currentUser

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.onAuthEvent(AuthEvent.GetCurrentUser)
        chatViewModel.onChatEvent(ChatEvent.GetAllPostPage)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show All Posts

        if (user != null) {
            val photoUrl = user.photoUrl?.toString()
            if (photoUrl != null) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Name: ${user.displayName ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
            Text("Email: ${user.email ?: "Not available"}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Post Input Form
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Post Title") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Post Description") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                chatViewModel.onChatEvent(
                    ChatEvent.UploadPost(
                        Post(
                            postTitle = title,
                            postDescription = description,
                            userId = user.uid
                        )
                    )
                )
                title = ""
                description = ""
            }) {
                Text("Submit Post")
            }

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { authViewModel.onAuthEvent(AuthEvent.SignOutButtonClick) }) {
                Text("Sign Out")
            }
        } else {
            Text("You are not logged in.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.LoginScreen.route) }) {
                Text("Sign In")
            }
        }
        Text("All Posts:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        chatState.allPosts.forEach {
            Text("• ${it.postTitle}: ${it.postDescription}")
            Spacer(modifier = Modifier.height(4.dp))
        }
        Button(onClick = {chatViewModel.onChatEvent(ChatEvent.GetAllPostPage)}) {
            Text(text= "Refresh")
        }
    }
}
