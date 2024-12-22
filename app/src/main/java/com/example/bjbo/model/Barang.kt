package com.example.bjbo.model

data class Barang(
    val id: String,
    val nama_barang: String,
    val kategori: String,
    val harga: Int,
    val deskripsi_barang: String,
    val stock: Int,
    val gambar: String
)
