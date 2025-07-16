package com.example.zappysearch.presentation.screens.components

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.*

@Composable
fun LocationPickerModal(
    initialLocation: LatLng?,
    isOwner: Boolean,
    onLocationSelected: (LatLng, String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { initializePlacesClient(context) }

    var markerPosition by remember { mutableStateOf(initialLocation ?: LatLng(20.5937, 78.9629)) }
    var address by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 5f)
    }
    val markerState = remember { MarkerState(position = markerPosition) }

    LaunchedEffect(query) {
        if (query.length > 2) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictions = response.autocompletePredictions
                }
                .addOnFailureListener {exception->
                    Log.e("PlacesSearch", "Prediction error: ${exception.message}", exception)

                    predictions = emptyList()
                }
        } else {
            predictions = emptyList()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Search location") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    predictions.forEach { prediction ->
                        Text(
                            text = prediction.getFullText(null).toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val placeId = prediction.placeId
                                    val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS)
                                    val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                                    placesClient.fetchPlace(request)
                                        .addOnSuccessListener { response ->
                                            response.place.latLng?.let {
                                                markerPosition = it
                                                markerState.position = it
                                                cameraPositionState.position =
                                                    CameraPosition.fromLatLngZoom(it, 15f)
                                                address = response.place.address ?: ""
                                                query = address
                                                predictions = emptyList()
                                            }
                                        }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = {
                            if (isOwner) {
                                markerPosition = it
                                markerState.position = it

                                val geocoder = Geocoder(context)
                                val result = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                                if (!result.isNullOrEmpty()) {
                                    address = result[0].getAddressLine(0)
                                    query = address
                                }
                            }
                        }
                    ) {
                        Marker(
                            state = markerState,
                            draggable = isOwner
                        )
                    }
                }

                if (isOwner) {
                    Button(
                        onClick = { onLocationSelected(markerPosition, address) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Save Location")
                    }
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
            }
        }
    }
}

private fun initializePlacesClient(context: Context): PlacesClient {
    val apiKey: String? = getApiKeyFromManifest(context)
    if (!Places.isInitialized()) {
        Places.initialize(context.applicationContext, apiKey ?: "")
    }
    return Places.createClient(context)
}

private fun getApiKeyFromManifest(context: Context): String? {
    val appInfo = context.packageManager
        .getApplicationInfo(context.packageName, android.content.pm.PackageManager.GET_META_DATA)
    return appInfo.metaData?.getString("com.google.android.geo.API_KEY")
}
