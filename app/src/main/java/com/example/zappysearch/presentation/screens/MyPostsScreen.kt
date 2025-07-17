package com.example.zappysearch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.toArg
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.geoPost.GeoPostViewModel
import com.example.zappysearch.presentation.navigation.Screen

@Composable
fun MyPostsScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    geoPostViewModel: GeoPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val myId = authViewModel.state.value.currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        chatViewModel.onChatEvent(ChatEvent.GetAllMyPosts(myId))
    }

    val myPosts = chatViewModel.chatState.value.myPosts

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(myPosts) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                onClick = {
                    navController
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("postForArg" , post.toArg())
                    navController.navigate(Screen.PostUploadUpdateScreen.route)
                }

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = post.postTitle, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = post.postDescription, style = MaterialTheme.typography.bodyMedium)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            chatViewModel.onChatEvent(ChatEvent.DeletePost(postId = post.postId))
                            geoPostViewModel.deleteGeoLocation(postId = post.postId)
                            chatViewModel.onChatEvent(ChatEvent.GetAllMyPosts(myId))

                         }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Post"
                            )
                        }
                    }
                }
            }
        }
    }
}
