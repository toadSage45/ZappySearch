package com.example.zappysearch.presentation.screens.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zappysearch.presentation.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screen.AllPostsScreen,
        Screen.MyChatsScreen,
        Screen.MyPostsScreen,
        Screen.ProfileScreen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        tonalElevation = 6.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.previousBackStackEntry?.destination?.route ?: return@navigate) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

