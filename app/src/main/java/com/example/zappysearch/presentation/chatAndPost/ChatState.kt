package com.example.zappysearch.presentation.chatAndPost


import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.domain.model.Chats
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.domain.model.Post

data class ChatState(
    val allPosts : List<Post> = emptyList<Post>(),
    val myChats : List<Chats> = emptyList<Chats>(),
    val singleUserMessages : List<Message> = emptyList<Message>(),
    val myPosts : List<Post> = emptyList<Post>(),
    val otherUser : AppUser? = null,
    val userDetailsMap : Map<String , AppUser> = emptyMap(),
    val geoPostId : String? = null
)