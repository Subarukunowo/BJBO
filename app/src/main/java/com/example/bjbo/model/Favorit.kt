package com.example.bjbo.model

import Postingan

data class FavoritResponse(
    val success: Boolean,           // Status keberhasilan API
    val message: String,            // Pesan dari API
    val data: List<Favorit>?        // Daftar data favorit (null jika tidak ada)
)

data class Favorit(
    var id: Int,                    // ID favorit
    val user_id: Int,               // ID pengguna
    val postingan_id: Int,          // ID postingan
    val created_at: String? = null, // Waktu data dibuat
    val updated_at: String? = null, // Waktu data diperbarui
    val user: User? = null,         // Data pengguna (null jika tidak dimuat)
    val postingan: Postingan? = null // Data postingan (null jika tidak dimuat)
)
