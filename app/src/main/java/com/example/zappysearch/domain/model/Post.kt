package com.example.zappysearch.domain.model

import com.google.firebase.Timestamp

data class Post(
    val postId : String = "",
    val postTitle : String = "",
    val postDescription : String = "",
    val timestamp: Timestamp?=null,
    val userId : String="",
    val imageUrl : String? = null ,
    val location : String? = null
)

