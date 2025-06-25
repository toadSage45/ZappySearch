package com.example.zappysearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.screens.AuthHomeScreen
import com.example.zappysearch.presentation.screens.AuthLoginScreen


@Composable
fun AppNavigation (authViewModel: AuthViewModel = hiltViewModel()) {
    val state = authViewModel.state.value

    val navController = rememberNavController()
    val startDestination : String = if(state.currentUser!=null){
        Screen.HomeScreen.route
    }else{
        Screen.LoginScreen.route
    }
    NavHost(navController = navController , startDestination = startDestination) {
        composable(route = Screen.HomeScreen.route){
            AuthHomeScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route){
            AuthLoginScreen(navController = navController)
        }
    }
}

@Preview
@Composable
fun AppNavigationPreview(){
    AppNavigation()
}