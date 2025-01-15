package com.example.bjbo.model

data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password: String,
    val alamat: String? = null,
    val kelamin: String? = null,
    val profile_picture: String? = null,
    val is_blocked: Boolean? = null,
    val remember_token: String? = null,
    val google_id: String? = null,
    val email_verified_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
