package com.example.zappysearch.domain.model

import com.google.firebase.Timestamp


data class Message(
    val messageId:String = "",
    val senderId : String = "",
    val textMessage : String = "" ,
    val timestamp: Timestamp?=null
)