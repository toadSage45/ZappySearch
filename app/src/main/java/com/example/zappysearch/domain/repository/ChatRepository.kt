package com.example.zappysearch.domain.repository

import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.domain.model.Chats
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.domain.model.Post
import com.google.firebase.auth.FirebaseUser

interface ChatRepository {

    suspend fun saveUser(user : AppUser) : Unit

    suspend fun getUserById(userId : String) : AppUser?

    suspend fun getAllPosts() : List<Post>

//    suspend fun getPostById(postId : String) : Post?

    suspend fun updatePost(post : Post) :String

    suspend fun getAllMyPosts(myId: String) : List<Post>

    suspend fun getAllMyChats(myId : String) : List<Chats>

    fun getSingleUserMessage(otherUserId : String,
                             myId : String ,
                             onMessagesChanged : (List<Message>) -> Unit,
                             onError : (Exception)->Unit) : Unit

    suspend fun uploadPost(post : Post) : String

//    suspend fun movePostToResolved(post : Post) :Unit

    suspend fun deletePost(postId : String) :Unit

    suspend fun startChat(otherUserId : String, myId : String) : Unit

    suspend fun sendMessage(otherUserId : String , myId : String , message : Message) : Unit

}