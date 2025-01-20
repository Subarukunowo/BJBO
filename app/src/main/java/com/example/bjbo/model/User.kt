package com.example.bjbo.model

data class UserRespons(
    val success: Boolean,
    val message: String,
    val data: User? // Tipe data generik
) {
    fun isSuccessful(): Boolean = success
}

data class User(
    val id: Int?,
    val name: String?,
    val email: String?,
    val password: String, // Don't expose this in the app directly
    val alamat: String? = null,
    val nomor_hp: String?=null,
    val kelamin: String? = null,
    val profile_picture: String? = null,
    val is_blocked: Int?,  // Change this to Int? to handle 0 or 1 values
    val remember_token: String? = null,
    val google_id: String? = null,
    val email_verified_at: String? = null,
    val created_at: String?,
    val updated_at: String?
) {
    val isBlocked: Boolean
        get() = is_blocked == 1 // Converts 1 to true, and 0 to false
}
