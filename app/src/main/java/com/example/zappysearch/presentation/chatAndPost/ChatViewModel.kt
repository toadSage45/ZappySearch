package com.example.zappysearch.presentation.chatAndPost

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zappysearch.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState



    fun onChatEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.DeletePost -> {
                viewModelScope.launch {
                    try {
                        chatRepository.deletePost(event.postId)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting post: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllMyChats -> {
                viewModelScope.launch {
                    try {
                        chatState.value.myId?.let { myId ->
                            val myChats = chatRepository.getAllMyChats(myId)
                            _chatState.value = chatState.value.copy(myChats = myChats)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching my chats: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllMyPostPage -> {
                viewModelScope.launch {
                    try {
                        chatState.value.myId?.let { myId ->
                            val myPosts = chatRepository.getAllMyPosts(myId)
                            _chatState.value = chatState.value.copy(myPosts = myPosts)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching my posts: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetAllPostPage -> {
                viewModelScope.launch {
                    try {
                        val allPosts = chatRepository.getAllPosts()
                        _chatState.value = chatState.value.copy(allPosts = allPosts)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching all posts: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.GetSingleUserMessages -> {
                viewModelScope.launch {
                    try {
                        val myId = chatState.value.myId
                        val otherUserId = chatState.value.otherUserId
                        if (!myId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                            val singleUserMessages = chatRepository.getSingleUserMessage(
                                otherUserId = otherUserId,
                                myId = myId
                            )
                            _chatState.value = chatState.value.copy(
                                singleUserMessages = singleUserMessages
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching messages: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.StartChat -> {
                viewModelScope.launch {
                    try {
                        val myId = chatState.value.myId
                        val otherUserId = chatState.value.otherUserId
                        if (!myId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                            chatRepository.startChat(myId = myId, otherUserId = otherUserId)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting chat: ${e.message}", e)
                    }
                }
            }

            is ChatEvent.SetMyUserId -> {
                _chatState.value = chatState.value.copy(myId = event.myId)
            }

            is ChatEvent.SetOtherUserId -> {
                _chatState.value = chatState.value.copy(otherUserId = event.otherUserId)
            }

            is ChatEvent.UploadPost -> {
                viewModelScope.launch {
                    try {
                        chatRepository.uploadPost(event.post)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error uploading post: ${e.message}", e)
                    }
                }
            }
        }
    }
    companion object{
        const val TAG = "ChatViewModel"
    }
}
