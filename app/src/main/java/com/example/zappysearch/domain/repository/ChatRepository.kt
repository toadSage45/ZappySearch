package com.example.zappysearch.domain.repository

import com.example.zappysearch.domain.model.Chats
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.domain.model.Post

interface ChatRepository {
    suspend fun getAllPosts() : List<Post>

    suspend fun getAllMyPosts(myId: String) : List<Post>

    suspend fun getAllMyChats(myId : String) : List<Chats>

    suspend fun getSingleUserMessage(otherUserId : String, myId : String) : List<Message>

    suspend fun uploadPost(post : Post) : String

    suspend fun movePostToResolved(post : Post) :Unit

    suspend fun deletePost(postId : String) :Unit

    suspend fun startChat(otherUserId : String, myId : String) : Unit

    suspend fun sendMessage(otherUserId : String , myId : String , message : Message) : Unit
}