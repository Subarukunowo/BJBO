package com.example.bjbo.model

data class Ulasan(
    val id: Int,                // ID ulasan
    val postingan_id: Int,      // ID postingan yang diulas
    val user_id: Int,           // ID pengguna yang memberikan ulasan
    val rating: Int,            // Rating ulasan (angka 1-5)
    val komentar: String,       // Komentar dari ulasan
    val created_at: String,     // Tanggal dan waktu ulasan dibuat
    val updated_at: String      // Tanggal dan waktu ulasan terakhir diperbarui
)

data class UlasanRequest(
    val userId: Int,
    val content: String,
    val rating: Int
)