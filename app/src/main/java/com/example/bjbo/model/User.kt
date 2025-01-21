package com.example.bjbo.model

data class UserRespons(
    val success: Boolean,
    val message: String,
    val data: User? // Tipe data generik
) {
    fun isSuccessful(): Boolean = success
}

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val alamat: String? = null,
    val nomor_hp: String? = null,
    val kelamin: String? = null,
    val profile_picture: String? = null,
    val is_blocked: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
 {
    val isBlocked: Boolean
        get() = is_blocked == 1 // Converts 1 to true, and 0 to false
}