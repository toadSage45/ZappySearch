package com.example.zappysearch.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.toArg
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.navigation.Screen
import com.example.zappysearch.presentation.screens.components.animations.LottieSearchAnimation


@Composable
fun AllPostsScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    // Load posts when screen is opened
    LaunchedEffect(Unit) {
        chatViewModel.onChatEvent(ChatEvent.GetAllPosts)
    }


    val myId = authViewModel.state.value.currentUser?.uid ?: ""
    val chatState = chatViewModel.chatState.value


    val posts = remember(chatState.allPosts, myId) {
        chatState.allPosts.filter { it.userId != myId }
    }

    if (posts.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("No posts from other users yet 😔", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item{
                LottieSearchAnimation()
            }
            items(posts) { post ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    onClick = {
                        navController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("postForArg", post.toArg())
                        navController.navigate(Screen.PostUploadUpdateScreen.route)
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = post.postTitle, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = post.postDescription, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

}
