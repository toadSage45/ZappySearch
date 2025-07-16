package com.example.zappysearch.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zappysearch.domain.model.Post
import com.example.zappysearch.domain.model.PostForArg
import com.example.zappysearch.domain.model.toGeoPoint
import com.example.zappysearch.presentation.auth.AuthViewModel
import com.example.zappysearch.presentation.chatAndPost.ChatEvent
import com.example.zappysearch.presentation.chatAndPost.ChatViewModel
import com.example.zappysearch.presentation.navigation.Screen
import com.example.zappysearch.presentation.screens.components.LocationPickerModal
import com.example.zappysearch.presentation.screens.components.StartChatIconButton
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostUploadUpdateScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context)
    }


    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val currentPost = navController.previousBackStackEntry
        ?.savedStateHandle?.get<PostForArg>("postForArg")

    val myId = authViewModel.state.value.currentUser?.uid?:""
    val myUserName = authViewModel.state.value.currentUser?.displayName?:"Anonymous"
    val isUpdating = currentPost!=null
    val isOwner  =  currentPost?.userId == myId || currentPost == null
    var showLocationPicker by remember { mutableStateOf<Boolean>(false) }

    var postTitle by remember { mutableStateOf("") }
    var postDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<LatLng?>(null) }
    var address by  remember { mutableStateOf<String>("")}
    var showLocationDialog by remember {mutableStateOf<Boolean>(false)}
    var getCurrentLocation by remember { mutableStateOf<Boolean>(false) }



        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let {
                        location = LatLng(it.latitude, it.longitude)
                    }
                }
            }
        }

        if(getCurrentLocation){
            LaunchedEffect(Unit) {
                val permissionStatus = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    // Already granted
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                        if(loc!=null) {
                            location = LatLng(loc.latitude, loc.longitude)
                        }else{
                            showLocationDialog = true
                        }
                    }
                }

                getCurrentLocation = false
            }
        }
    LaunchedEffect(location) {location?.let {loc->
        val geocoder = Geocoder(context)
        val result = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
        if (!result.isNullOrEmpty()) {
            address = result[0].getAddressLine(0)
        }

    } }


    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Location Unavailable") },
            text = { Text("Couldn't access your location. Please set it manually.") },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("OK")
                }
            }
        )
    }


    LaunchedEffect(currentPost) {
        currentPost?.let {
            postTitle = it.postTitle
            postDescription = it.postDescription
            location = it.location?.let { loc -> LatLng(loc.latitude, loc.longitude) }
        }
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
                value = location?.let { "${it.latitude}, ${it.longitude} , $address" } ?: "",
                onValueChange = {}, // No typing allowed
                label = { Text("Location") },
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = isOwner) {

                        showLocationPicker = true
                    }
            )

            if (isOwner) {
                Button(
                    onClick = {
                        if (!getCurrentLocation) getCurrentLocation = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Current Location")
                }
            }


            if (isOwner) {
                Button(
                    onClick = {
                        val post = Post(
                            postId = currentPost?.postId ?: "",
                            postTitle = postTitle.trim(),
                            postDescription = postDescription.trim(),
                            location = location?.toGeoPoint(),
                            userId = currentPost?.userId ?: myId,
                            timestamp = currentPost?.timestamp ?: Timestamp.now(),
                            userName = myUserName,
                            address = address
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

        if (showLocationPicker) {
            Dialog(onDismissRequest = { showLocationPicker = false }) {
                LocationPickerModal(
                    initialLocation = location,
                    isOwner = isOwner,
                    onLocationSelected = { latLng, addr ->
                        location = latLng
                        address = addr
                        showLocationPicker = false
                    },
                    onDismiss = { showLocationPicker = false }
                )
            }
        }

    }
}


