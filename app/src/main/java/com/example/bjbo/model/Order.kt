package com.example.bjbo.model

data class Order(
    val id: Int,
    val user_id: Int,
    val postingan_id: Int,
    val total_harga: Double,
    val status: String,
    val snap_token: String?, // Pastikan nama properti sesuai dengan JSON
    val redirect_url: String? // Tambahkan jika diperlukan
)

