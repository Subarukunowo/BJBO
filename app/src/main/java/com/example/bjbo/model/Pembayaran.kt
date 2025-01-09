package com.example.bjbo.model

data class Pembayaran(
    val id_barang: String,
    val nama_barang: String,
    val harga_barang: Int,
    val biaya_aplikasi: Int = 500,
    val metode_pembayaran: String,
    val biaya_tambahan: Int = 500,
    val totalPembayaran: Int = harga_barang + biaya_aplikasi + biaya_tambahan,
    val status: String = "pending"
)

