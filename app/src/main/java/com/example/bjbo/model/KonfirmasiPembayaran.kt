package com.example.bjbo.model


data class KonfirmasiPembayaran(
    val idBarang: String,
    val namaBarang: String,
    val trackingId: String,
    val orderId: String,
    val totalPembayaran: Int
)
