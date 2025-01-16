package com.example.bjbo.model

data class ProfileRequest(

    val name: String,
    val email: String,
    val alamat: String,
    val kelamin: String,
    val profile_picture: String? = null // Opsional untuk gambar profil
)
