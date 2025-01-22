package com.example.bjbo.model
import com.example.bjbo.model.User

// Model utama untuk ulasan
// Model untuk data pengguna terkait ulasan
data class UlasanUser(
    val id: Int,
    val name: String,
    val email: String
)



// Model utama untuk ulasan
data class Ulasan(
    val id: Int,
    val postingan_id: Int,
    val user_id: Int,
    val rating: Int,
    val komentar: String,
    val created_at: String?,
    val updated_at: String?,
    val user: User?,           // Menggunakan model User dari namespace ulasan
    val postingan: Postingan?  // Menyertakan data postingan terkait
)


// Model untuk data postingan terkait ulasan
data class Postingan(
    val id: Int,
    val name: String
)

// Model untuk permintaan penyimpanan atau pembaruan ulasan
data class UlasanRequest(
    val postingan_id: Int,
    val user_id: Int,
    val rating: Int,
    val komentar: String
)

// Model generik untuk membungkus respons API
data class ApiResponse<T>(
    val success: Boolean,   // Indikator keberhasilan
    val message: String?,    // Pesan untuk pengguna
    val data: T?            // Data utama
)
