// MyChatsScreen.kt
package com.example.zappysearch.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.navigation.Screen

@Composable
fun MyChatsScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val myId = authViewModel.state.value.currentUser?.uid ?: ""

    LaunchedEffect(myId) {
        chatViewModel.onChatEvent(ChatEvent.GetAllMyChats(myId))
    }


    val chats = chatViewModel.chatState.value.myChats
    val userMap = chatViewModel.chatState.value.userDetailsMap


    LaunchedEffect(chats) {
        Log.d("MyChatsScreen", "Loaded ${chats.size} chats")
        chats.forEach { chat ->
            Log.d("MyChatsScreen", "Chat ID: ${chat.chatId}, Participants: ${chat.participants}")
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(chats) { chat ->
            val otherUserId = chat.participants.firstOrNull { it != myId }
            val otherUserName = userMap[otherUserId]?.name ?: "Unknown User"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (otherUserId != null) {
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("otherUserId", otherUserId)
                            navController.navigate(Screen.SingleUserMessageScreen.route)
                        }
                    },
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = otherUserName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Chat ID: ${chat.chatId.take(10)}...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
