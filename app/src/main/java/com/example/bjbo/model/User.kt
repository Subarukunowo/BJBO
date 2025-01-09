package com.example.bjbo.model

import java.io.Serializable

data class User(
    val id_user: String,
    val username: String = "", // Kosong untuk sementara
    val password: String,
    val nama_depan: String = "", // Kosong untuk sementara
    val nama_belakang: String = "", // Kosong untuk sementara
    val email: String,
    val nomor_hp: String = "", // Kosong untuk sementara
    val foto_profil: String? = null // Kosong untuk sementara
)
