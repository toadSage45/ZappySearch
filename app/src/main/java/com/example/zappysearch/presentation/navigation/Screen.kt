package com.example.zappysearch.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route : String, val title : String="", val icon : ImageVector) {
    object ProfileScreen : Screen(route = "profile_screen" , title ="Profile" , Icons.Default.AccountCircle)
    object AllPostsScreen : Screen(route = "all_posts_screen" , title = "Home" , Icons.Default.Home)
    object MyChatsScreen : Screen(route = "my_chats_screen" , title = "Chats" , Icons.Default.MailOutline)
    object MyPostsScreen : Screen(route ="my_posts_screen" , title = "MyPosts" , Icons.Default.Face)
    object SingleUserMessageScreen : Screen(route = "single_user_message_screen" , icon = Icons.Default.Send)
    object PostUploadUpdateScreen : Screen(route = "post_upload_update_screen" ,title = "NewPost" , icon = Icons.Default.AddCircle)
}