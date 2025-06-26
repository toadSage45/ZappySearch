package com.example.zappysearch.domain.model

data class Chats(
    val participants : List<String> = emptyList(),
    val chatId : String = ""
)