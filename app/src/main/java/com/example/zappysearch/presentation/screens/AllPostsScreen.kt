package com.example.zappysearch.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.toArg
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.geoPost.GeoPostViewModel
import com.example.zappysearch.presentation.navigation.Screen
import com.example.zappysearch.presentation.screens.components.LocationPickerModal
import com.example.zappysearch.presentation.screens.components.animations.LottieSearchAnimation
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng

@Composable
fun AllPostsScreen(
    chatViewModel: ChatViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    geoPostViewModel: GeoPostViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    // ✅ Map Init Debug
    LaunchedEffect(Unit) {
        Log.d("AllPostsScreen", "Initializing Maps...")
        MapsInitializer.initialize(context)
        chatViewModel.onChatEvent(ChatEvent.GetAllPosts)
        Log.d("AllPostsScreen", "ChatEvent.GetAllPosts triggered")
    }


    val myId = authViewModel.state.value.currentUser?.uid ?: ""
    val chatState = chatViewModel.chatState.value

    var showLocationPicker by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf<LatLng?>(null) }
    var address by remember { mutableStateOf("") }
    var radiusText by remember { mutableStateOf("5.0") }

    val posts = remember(chatState.allPosts, myId) {
        chatState.allPosts.filter { it.userId != myId }
    }



    Log.d("AllPostsScreen", "Rendering Scaffold with ${posts.size} posts")

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        showLocationPicker = true
                        Log.d("AllPostsScreen", "Location picker opened")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pick")
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = {},
                    label = { Text("Location") },
                    readOnly = true,
                    modifier = Modifier.weight(2f)
                )
            }



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = radiusText,
                    onValueChange = {
                        radiusText = it
                        Log.d("AllPostsScreen", "Radius updated to $radiusText")
                    },
                    label = { Text("Radius (in km)") },
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )
                Button(
                    onClick = {

                        Log.d("AllPostsScreen", "Searching Nearby Posts")


                    },
                    modifier = Modifier.weight(1f),
                    enabled = address.isNotBlank() && location!=null && radiusText.isNotBlank()
                ) {
                    Text("Search Nearby Posts")
                }


            }
            if (address.isBlank() || location == null || radiusText.isBlank()) {
                Text("📍 Select a location and radius to search nearby posts")
            }





            Spacer(modifier = Modifier.height(16.dp))

            if (posts.isEmpty()) {
                Log.d("AllPostsScreen", "No posts to display")
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No posts from other users yet 😔",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        LottieSearchAnimation()
                    }

                    items(posts) { post ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.elevatedCardElevation(8.dp),
                            onClick = {
                                Log.d("AllPostsScreen", "Post clicked: ${post.postTitle}")
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("postForArg", post.toArg())
                                navController.navigate(Screen.PostUploadUpdateScreen.route)
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = post.postTitle,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = post.postDescription,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showLocationPicker) {
            Log.d("AllPostsScreen", "Showing LocationPickerModal")
            Dialog(
                onDismissRequest = {
                    showLocationPicker = false
                    Log.d("AllPostsScreen", "Location picker dismissed")
                },
                properties = DialogProperties(usePlatformDefaultWidth = true)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    tonalElevation = 4.dp
                ) {
                    LocationPickerModal(
                        initialLocation = location,
                        isOwner = true,
                        onLocationSelected = { latLng, addr ->
                            location = latLng
                            address = addr
                            showLocationPicker = false
                            Log.d("AllPostsScreen", "Location selected: $latLng ($addr)")
                        },
                        onDismiss = {
                            showLocationPicker = false
                            Log.d("AllPostsScreen", "Dismiss clicked in modal")
                        }
                    )
                }
            }
        }
    }
}
