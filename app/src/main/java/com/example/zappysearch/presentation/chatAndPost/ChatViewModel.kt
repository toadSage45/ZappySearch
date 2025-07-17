package com.example.zappysearch.presentation.chatAndPost

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zappysearch.domain.model.AppUser
import com.example.zappysearch.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    init{
        onChatEvent(ChatEvent.GetAllPosts)
    }


    fun onChatEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.DeletePost -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                        chatRepository.deletePost(event.postId)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting post: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllMyChats -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                            val myChats = chatRepository.getAllMyChats(event.myId)
                            _chatState.value = chatState.value.copy(myChats = myChats)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching my chats: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllMyPosts -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                            val myPosts = chatRepository.getAllMyPosts(event.myId)
                            _chatState.value = chatState.value.copy(myPosts = myPosts)

                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching my posts: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllPosts -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                        val allPosts = chatRepository.getAllPosts()

                        val userIdSet = allPosts.map{it.userId}.toSet()

                        val userDetailsMap = mutableMapOf<String , AppUser>()

                        for(userId in userIdSet){
                            if(!chatState.value.userDetailsMap.containsKey(userId)){
                                val user = chatRepository.getUserById(userId)
                                if(user!=null){
                                    userDetailsMap[userId] = user
                                }
                            }
                        }

                        _chatState.value = chatState.value.copy(allPosts = allPosts,
                            userDetailsMap = chatState.value.userDetailsMap + userDetailsMap)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching all posts: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetSingleUserMessages -> {
                chatRepository.getSingleUserMessage(
                    otherUserId = event.otherUserId,
                    myId = event.myId,
                    onMessagesChanged = { messages ->
                        _chatState.value = chatState.value.copy(singleUserMessages = messages)
                    },
                    onError = { e ->
                        Log.e(TAG, "Error listening to messages: ${e.message}")
                    }
                )
            }

            is ChatEvent.StartChat -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                        val myId = event.myId
                        val otherUserId = event.otherUserId
                        chatRepository.startChat(myId = myId, otherUserId = otherUserId)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting chat: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.UploadPost -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val geoPostId = chatRepository.uploadPost(event.post)
                        _chatState.value = chatState.value.copy(geoPostId = geoPostId)
                        //onChatEvent(ChatEvent.GetAllMyPosts(event.myId))
                    } catch (e: Exception) {
                        Log.e(TAG, "Error uploading post: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.UpdatePost -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val geoPostId = chatRepository.updatePost(event.post)
                        //onChatEvent(ChatEvent.GetAllMyPosts(event.myId))
                        _chatState.value = chatState.value.copy(geoPostId = geoPostId)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error uploading post: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetUserInfo -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                       val otherUser =  chatRepository.getUserById(event.userId)
                        _chatState.value = chatState.value.copy(
                            otherUser = otherUser
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error Fetching Other User: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.SetUserInfo -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                       chatRepository.saveUser(event.user)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error uploading user Info: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.SendMessage -> {
                viewModelScope.launch (Dispatchers.IO){
                    try {
                        chatRepository.sendMessage(myId = event.myId,
                            otherUserId = event.otherUserId ,
                            message = event.message)
                    }catch (e: Exception) {
                        Log.e(TAG, "Error sending the message: ${e.message}", e)
                    }
                }
            }

            ChatEvent.ClearGeoPostId -> {
                viewModelScope.launch(Dispatchers.IO){
                    _chatState.value = chatState.value.copy(geoPostId = null)
                }
            }
        }
    }
    companion object{
        const val TAG = "ChatViewModel"
    }
}
