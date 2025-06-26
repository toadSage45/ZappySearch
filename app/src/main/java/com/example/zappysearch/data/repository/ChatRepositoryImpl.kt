package com.example.zappysearch.data.repository

import android.util.Log
import com.example.zappysearch.domain.model.Chats
import com.example.zappysearch.domain.model.Message
import com.example.zappysearch.domain.model.Post
import com.example.zappysearch.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



class ChatRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) : ChatRepository {

    override suspend fun getAllPosts(): List<Post> {
        return try {
            val snapshot = firestore.collection("posts").get().await()
            snapshot.documents.mapNotNull { it.toObject(Post::class.java)?.copy(postId = it.id) }
        } catch (e: Exception) {
            Log.e(TAG, "getAllPosts: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getAllMyPosts(myId: String): List<Post> {
        return try {
            val snapshot = firestore.collection("posts").whereEqualTo("userId", myId).get().await()
            snapshot.documents.mapNotNull { it.toObject(Post::class.java)?.copy(postId = it.id) }
        } catch (e: Exception) {
            Log.e(TAG, "getAllMyPosts: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getAllMyChats(myId: String): List<Chats> {
        return try {
            val snapshot = firestore.collection("chats")
                .whereArrayContains("participants", myId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Chats::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "getAllMyChats: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getSingleUserMessage(otherUserId: String, myId: String): List<Message> {
        return try {
            val chatId = listOf(myId, otherUserId).sorted().joinToString("_")
            val snapshot = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "getSingleUserMessage: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun uploadPost(post: Post): String {
        return try {
            val postId = firestore.collection("posts").document().id
            val postWithId = post.copy(postId = postId)
            firestore.collection("posts").document(postId).set(postWithId).await()
            postId
        } catch (e: Exception) {
            Log.e(TAG, "uploadPost: ${e.message}", e)
            ""
        }
    }

    override suspend fun movePostToResolved(post: Post) {
        try {
            firestore.collection("resolved-posts").add(post).await()
            deletePost(post.postId)
        } catch (e: Exception) {
            Log.e(TAG, "movePostToResolved: ${e.message}", e)
        }
    }

    override suspend fun deletePost(postId: String) {
        try {
            firestore.collection("posts")
                .document(postId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "deletePost: ${e.message}", e)
        }
    }

    override suspend fun startChat(otherUserId: String, myId: String) {
        try {
            val chatId = listOf(myId, otherUserId).sorted().joinToString("_")
            val chatWithId = Chats(
                participants = listOf(myId, otherUserId),
                chatId = chatId
            )
            val chatDoc = firestore.collection("chats")
                .document(chatId)
                .get()
                .await()

            if (!chatDoc.exists()) {
                firestore.collection("chats")
                    .document(chatId)
                    .set(chatWithId)
                    .await()
            }
        } catch (e: Exception) {
            Log.e(TAG, "startChat: ${e.message}", e)
        }
    }

    override suspend fun sendMessage(otherUserId: String, myId: String, message: Message) {
        try {
            val chatId = listOf(myId, otherUserId).sorted().joinToString("_")
            val messageId = firestore.collection("chats")
                .document(chatId).collection("messages").document().id

            val messageWithId = message.copy(messageId = messageId)

            firestore.collection("chats")
                .document(chatId).collection("messages")
                .document(messageId).set(messageWithId).await()
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage: ${e.message}", e)
        }
    }

    companion object {
        private const val TAG = "ChatRepository"
    }
}
