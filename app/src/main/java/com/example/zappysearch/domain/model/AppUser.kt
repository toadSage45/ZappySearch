package com.example.zappysearch.domain.model

data class AppUser(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null
)