package com.example.zappysearch.presentation.screens


import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.zappysearch.R
import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.domain.model.PostForArg
import com.example.zappysearch.presentation.auth.AuthEvent
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.navigation.Screen

@Composable
fun AuthProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val authState = authViewModel.state.value
    val user = authState.currentUser
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        authViewModel.onAuthEvent(AuthEvent.GetCurrentUser)
    }

    LaunchedEffect(user) {
        user?.let {
            chatViewModel.onChatEvent(
                ChatEvent.SetUserInfo(
                    AppUser(
                        userId = it.uid,
                        email = it.email ?: "No email",
                        name = it.displayName ?: "No Name",
                        photoUrl = it.photoUrl.toString()
                    )
                )
            )
        }
    }



    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp), // space for image overlap
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 56.dp, bottom = 24.dp, start = 16.dp, end = 16.dp), // top padding below image
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = user.displayName?.uppercase() ?: "UNKNOWN",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        shadow = Shadow(
                                            color = Color.Gray,
                                            offset = Offset(2f, 2f),
                                            blurRadius = 4f
                                        ),
                                        letterSpacing = 1.2.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )

                                Text(user.email ?: "Not available",
                                    style = TextStyle(color = Color(0xFF21F328)))

                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = { authViewModel.onAuthEvent(AuthEvent.SignOutButtonClick) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                topStart = 24.dp,
                                                topEnd = 24.dp,
                                                bottomStart = 0.dp,
                                                bottomEnd = 0.dp
                                            )
                                        ),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                    ) {
                                    Text("Sign Out", color = Color.White)
                                }
                            }
                        }

                        // Overlapping Profile Image
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Edit button
                            Button(
                                onClick = { },
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Center: Profile Image
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.default_profile_image)
                                    .crossfade(true)
                                    .placeholder(R.drawable.default_profile_image)
                                    .build(),
                                contentDescription = "Firebase User Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(Color.White, shape = CircleShape)
                                    .padding(4.dp)
                                    .shadow(8.dp, CircleShape, clip = false)
                            )

                            // Right: Add button
                            Button(
                                onClick = { },
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21F328)
                                ),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }


                    }
                }
            }
            else {
                Text("You are not logged in.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    activity?.let{activity->
                        authViewModel.onAuthEvent(AuthEvent.LoginButtonClick(activity = activity))
                    }

                }) {
                    Text("Sign In with Google")
                }
            }


        }
    }
}
