package com.example.zappysearch.domain.model


import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


data class Post(
    val postId : String = "",
    val postTitle : String = "",
    val postDescription : String = "",
    val timestamp: Timestamp?=null,
    val userId : String="",
    val imageUrl : String = "" ,
    val location : String = "",
    val userName: String =""
)

@Parcelize
data class PostForArg(
    val postId : String = "",
    val postTitle : String = "",
    val postDescription : String = "",
    val timestamp: Timestamp?=null,
    val userId : String="",
    val imageUrl : String = "" ,
    val location : String = "",
    val userName: String =""
) : Parcelable

fun Post.toArg(): PostForArg = PostForArg(
    postId,
    postTitle,
    postDescription,
    timestamp,
    userId,
    imageUrl,
    location
)

fun PostForArg.toPost(): Post = Post(
    postId,
    postTitle,
    postDescription,
    timestamp,
    userId,
    imageUrl,
    location
)


