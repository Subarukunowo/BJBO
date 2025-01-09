package com.example.bjbo.model

data class PembayaranResponse(
    val status: String,
    val snap_token: String? = null // Hanya ada jika E-Wallet digunakan
)
