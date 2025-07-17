package com.example.zappysearch.presentation.geoPost

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.zappysearch.data.repository.GeoPostApi
import com.example.zappysearch.data.repository.GeoPostBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeoPostViewModel @Inject constructor(
    private val api: GeoPostApi
) : ViewModel() {

    fun saveGeoLocation(postId: String, lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val response = api.saveGeoPost(GeoPostBody(postId, lat, long))
                if (response.isSuccessful) {
                    Log.d("GeoPostViewModel", "✅ Geo location saved successfully")
                } else {
                    Log.e("GeoPostViewModel", "❌ API error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GeoPostViewModel", "❌ Network error while saving location: ${e.localizedMessage}")
            }
        }
    }

    fun deleteGeoLocation(postId: String) {
        viewModelScope.launch {
            try {
                val response = api.deleteGeoPost(postId)
                if (response.isSuccessful) {
                    Log.d("GeoPostViewModel", "✅ Geo location deleted successfully")
                } else {
                    Log.e("GeoPostViewModel", "❌ API error on delete: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GeoPostViewModel", "❌ Network error while deleting location: ${e.localizedMessage}")
            }
        }
    }

    fun getNearbyPosts(
        lat: Double,
        long: Double,
        radius: Double,
        onResult: (List<String>) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val response = api.getNearbyPosts(lat, long, radius)
                if (response.isSuccessful) {
                    val postIds = response.body()?.postIds.orEmpty()
                    Log.d("GeoPostViewModel", "✅ Nearby posts fetched: $postIds")
                    onResult(postIds)
                } else {
                    Log.e("GeoPostViewModel", "❌ API error on fetch: ${response.code()} - ${response.message()}")
                    onResult(emptyList())
                }
            } catch (e: Exception) {
                Log.e("GeoPostViewModel", "❌ Network error while fetching nearby posts: ${e.localizedMessage}")
                onResult(emptyList())
            }
        }
    }
}
