package com.example.zappysearch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.google.firebase.Timestamp


@Composable
fun SingleUserMessageScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val myId = authViewModel.state.value.currentUser?.uid ?: ""
    val chatState = chatViewModel.chatState.value
    val messages = chatState.singleUserMessages
    val otherUserDetails = chatState.otherUser

    val otherUserId = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("otherUserId") ?: ""

    var input by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(otherUserId) {
        chatViewModel.onChatEvent(ChatEvent.GetSingleUserMessages(otherUserId, myId))
        chatViewModel.onChatEvent(ChatEvent.GetUserInfo(otherUserId))
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat with ${otherUserDetails?.name ?: "Unknown User"}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No messages yet", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = listState
            ) {
                items(messages) { message ->
                    val isMyMessage = message.senderId == myId
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
                    ) {
                        Text(
                            text = message.textMessage,
                            color = if (isMyMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = if (isMyMessage) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (input.isNotBlank()) {
                            chatViewModel.onChatEvent(
                                ChatEvent.SendMessage(
                                    myId = myId,
                                    otherUserId = otherUserId,
                                    message = Message(
                                        textMessage = input,
                                        senderId = myId,
                                        timestamp = Timestamp.now()
                                    )
                                )
                            )
                            input = ""
                            focusManager.clearFocus()
                        }
                    }
                )
            )
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        chatViewModel.onChatEvent(
                            ChatEvent.SendMessage(
                                myId = myId,
                                otherUserId = otherUserId,
                                message = Message(
                                    textMessage = input,
                                    senderId = myId,
                                    timestamp = Timestamp.now()
                                )
                            )
                        )
                        input = ""
                        focusManager.clearFocus()
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
