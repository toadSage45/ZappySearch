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


@Composable
fun AuthHomeScreen(authViewModel: AuthViewModel = hiltViewModel(),
                   navController: NavController) {
    val state = authViewModel.state.value
    val user = state.currentUser

    LaunchedEffect(state.currentUser) {
        authViewModel.onAuthEvent(AuthEvent.GetCurrentUser)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            //Image
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

            //Name and Email
            Text(text = "Name: ${user.displayName ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Email: ${user.email ?: "Not available"}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            //Sign Out Button
            Button(onClick = { authViewModel.onAuthEvent(AuthEvent.SignOutButtonClick) }) {
                Text("Sign Out")
            }
        } else {
            // Not Logged In
            Text(text = "You are not logged in.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.LoginScreen.route) }) {
                Text("Sign In")
            }
        }
    }
}
