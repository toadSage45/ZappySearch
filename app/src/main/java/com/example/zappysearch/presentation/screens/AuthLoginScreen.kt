package com.example.zappysearch.presentation.screens

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.presentation.auth.AuthEvent
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.navigation.Screen


@Composable
fun AuthLoginScreen(authViewModel: AuthViewModel = hiltViewModel(),
                    navController: NavController) {
    val state = authViewModel.state.value
    val activity = LocalActivity.current
    LaunchedEffect(state.currentUser) {
        if(state.currentUser!=null)
        {
            navController.navigate(Screen.HomeScreen.route){
                popUpTo(Screen.LoginScreen.route){
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Welcome to ZappySearch", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            activity?.let{activity->
                authViewModel.onAuthEvent(AuthEvent.LoginButtonClick(activity = activity))
            }

        }) {
            Text("Sign In with Google")
        }
    }
}
