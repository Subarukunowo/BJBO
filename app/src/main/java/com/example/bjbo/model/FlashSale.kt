package com.example.bjbo.model


data class FlashSale(
    val id: String,
    val id_flashsale: String,
    val id_barang: String,
    val nama_barang: String,
    val harga_barang: Double,
    val harga_flashsale: Double,
    val stok_flashsale: Int,
    val waktu_mulai: String,
    val waktu_berakhir: String,
    val gambar: String
)
