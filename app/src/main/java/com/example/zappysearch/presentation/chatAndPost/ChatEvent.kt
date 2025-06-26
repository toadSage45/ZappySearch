package com.example.zappysearch.presentation.chatAndPost

import com.example.zappysearch.domain.model.Post

sealed class ChatEvent {
    object GetAllPostPage : ChatEvent()
    object GetAllMyPostPage : ChatEvent()
    object GetAllMyChats : ChatEvent()
    object GetSingleUserMessages : ChatEvent()
    data class SetOtherUserId(val otherUserId : String) : ChatEvent()
    data class SetMyUserId(val myId : String) : ChatEvent()
    data class UploadPost(val post : Post) : ChatEvent()
    data class DeletePost(val postId : String) : ChatEvent()
    object StartChat : ChatEvent()

}