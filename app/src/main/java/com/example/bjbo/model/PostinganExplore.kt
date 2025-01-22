package com.example.bjbo.model

data class PostinganExplore(
    val id: Int,
    val name: String,
    val price: Long,
    val category: String,
    val description: String,
    var image: String?,
    val lokasi: String,
    val user_id: Int,
    val username: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
)
