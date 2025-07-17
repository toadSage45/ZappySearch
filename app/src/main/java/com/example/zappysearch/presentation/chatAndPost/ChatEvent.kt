package com.example.zappysearch.presentation.chatAndPost

import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.domain.model.Post

sealed class ChatEvent {
    object GetAllPosts : ChatEvent()
    data class GetAllMyPosts(val myId : String) : ChatEvent()
    data class GetAllMyChats(val myId : String) : ChatEvent()
    data class GetSingleUserMessages(val myId : String , val otherUserId : String) : ChatEvent()
    data class UploadPost(val post : Post  , val myId : String) : ChatEvent()
    data class UpdatePost(val post : Post , val myId :String) : ChatEvent()
    data class DeletePost(val postId : String) : ChatEvent()
    data class StartChat(val myId : String , val otherUserId : String) : ChatEvent()
    data class GetUserInfo(val userId : String) : ChatEvent()
    data class SetUserInfo(val user : AppUser) : ChatEvent()
    data class SendMessage(val myId : String , val otherUserId: String , val message : Message) : ChatEvent()
    object ClearGeoPostId : ChatEvent()
}