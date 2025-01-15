package com.example.bjbo.model

data class OrderRequest(
    val user_id: Int,
    val postingan_id: Int,
    val total_harga: Double,
    val status: String
)
