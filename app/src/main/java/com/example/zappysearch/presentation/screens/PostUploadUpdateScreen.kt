package com.example.zappysearch.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.Post
import com.example.zappysearch.domain.model.PostForArg
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.navigation.Screen
import com.example.zappysearch.presentation.screens.components.StartChatIconButton
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostUploadUpdateScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentPost = navController.previousBackStackEntry
        ?.savedStateHandle?.get<PostForArg>("postForArg")

    val myId = authViewModel.state.value.currentUser?.uid?:""
    val myUserName = authViewModel.state.value.currentUser?.displayName?:"Anonymous"
    val isUpdating = currentPost!=null
    val isOwner  =  currentPost?.userId == myId || currentPost == null

    var postTitle by remember { mutableStateOf("") }
    var postDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    if(currentPost!=null) {
        postTitle = currentPost.postTitle
        postDescription = currentPost.postDescription
        location = currentPost.location
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (isUpdating) "Post Details" else "New Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = postTitle,
                onValueChange = { postTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isOwner
            )

            OutlinedTextField(
                value = postDescription,
                onValueChange = { postDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                enabled = isOwner
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isOwner
            )

            if (isOwner) {
                Button(
                    onClick = {
                        val post = Post(
                            postId = currentPost?.postId ?: "",
                            postTitle = postTitle.trim(),
                            postDescription = postDescription.trim(),
                            location = location.trim(),
                            userId = currentPost?.userId ?: myId,
                            timestamp = currentPost?.timestamp ?: Timestamp.now(),
                            userName = currentPost?.userName?:myUserName
                        )

                        scope.launch {
                            if (isUpdating) {
                                chatViewModel.onChatEvent(ChatEvent.UpdatePost(post=post,myId = myId))
                                snackBarHostState.showSnackbar("Post updated")
                            } else {
                                chatViewModel.onChatEvent(ChatEvent.UploadPost(post = post , myId=myId))
                                snackBarHostState.showSnackbar("Post created")
                            }

                            chatViewModel.onChatEvent(ChatEvent.GetAllMyPosts(myId= myId))
                        }
                        navController.navigate(Screen.MyPostsScreen.route) {
                            popUpTo(Screen.MyPostsScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = postTitle.isNotBlank() && postDescription.isNotBlank()
                ) {
                    Text(if (isUpdating) "Update Post" else "Create Post")
                }
            }else{
                fun onClick(){
                    chatViewModel.onChatEvent(ChatEvent.StartChat(myId = myId , otherUserId = currentPost.userId))
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("otherUserId" , currentPost.userId)

                    navController.navigate(Screen.SingleUserMessageScreen.route)
                }
                StartChatIconButton(onClick = {onClick()})
            }
        }
    }
}


