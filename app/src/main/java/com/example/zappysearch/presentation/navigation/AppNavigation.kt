package com.example.zappysearch.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zappysearch.domain.model.PostForArg
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.screens.AllPostsScreen
import com.example.zappysearch.presentation.screens.AuthProfileScreen
import com.example.zappysearch.presentation.screens.MyChatsScreen
import com.example.zappysearch.presentation.screens.MyPostsScreen
import com.example.zappysearch.presentation.screens.PostUploadUpdateScreen
import com.example.zappysearch.presentation.screens.SingleUserMessageScreen
import com.example.zappysearch.presentation.screens.components.BottomNavBar

@Composable
fun AppNavigation(authViewModel: AuthViewModel = hiltViewModel()
) {
    val state = authViewModel.state.value

    val navController = rememberNavController()
    val startDestination: String = if (state.currentUser != null) {
        Screen.ProfileScreen.route
    } else {
        Screen.AllPostsScreen.route
    }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val showFab = currentRoute == Screen.AllPostsScreen.route || currentRoute == Screen.MyPostsScreen.route

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(onClick = {
                    val postForArg : PostForArg? = null
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("postForArg", postForArg)

                    navController.navigate(Screen.PostUploadUpdateScreen.route)
                }) {
                    Icon(Screen.PostUploadUpdateScreen.icon, contentDescription = "New Post")
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.ProfileScreen.route) {
                AuthProfileScreen()
            }

            composable(route = Screen.AllPostsScreen.route) {
                    AllPostsScreen(navController = navController)
            }
            composable(route = Screen.MyChatsScreen.route) {
                MyChatsScreen(navController = navController)
            }
            composable(route = Screen.MyPostsScreen.route) {
                MyPostsScreen(navController = navController)
            }
            composable(route = Screen.SingleUserMessageScreen.route) {
                SingleUserMessageScreen(navController = navController)
            }
            composable(route = Screen.PostUploadUpdateScreen.route) {
                PostUploadUpdateScreen(navController = navController)
            }
        }
    }
}

@Preview
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}
