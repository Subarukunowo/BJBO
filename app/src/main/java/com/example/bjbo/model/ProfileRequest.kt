package com.example.bjbo.model

data class ProfileRequest(

    val name: String,
    val email: String,
    val nomor_hp: String,
    val alamat: String,
    val kelamin: String,
    val profile_picture: String? = null // Opsional untuk gambar profil
)
