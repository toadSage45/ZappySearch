package com.example.zappysearch.domain.model


import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


data class Post(
    val postId : String = "",
    val postTitle : String = "",
    val postDescription : String = "",
    val timestamp: Timestamp?=null,
    val userId : String="",
    val imageUrl : String = "" ,
    val location : GeoPoint? = null,
    val userName: String ="",
    val address : String = ""
)


data class GeoPoint(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)

@Parcelize
data class PostForArg(
    val postId : String = "",
    val postTitle : String = "",
    val postDescription : String = "",
    val timestamp: Timestamp?=null,
    val userId : String="",
    val imageUrl : String = "",
    val location : @RawValue GeoPoint? = null,
    val userName: String ="",
    val address: String = ""
) : Parcelable

fun Post.toArg(): PostForArg = PostForArg(
    postId,
    postTitle,
    postDescription,
    timestamp,
    userId,
    imageUrl,
    location,
    address
)

fun PostForArg.toPost(): Post = Post(
    postId,
    postTitle,
    postDescription,
    timestamp,
    userId,
    imageUrl,
    location,
    address
)

fun LatLng.toGeoPoint(): GeoPoint {
    return GeoPoint(latitude = this.latitude, longitude = this.longitude)
}

