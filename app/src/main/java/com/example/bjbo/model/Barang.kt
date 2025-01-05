package com.example.bjbo.model

data class Barang(
    val id_barang: String,
    val nama_barang: String,
    val kategori: String,
    val harga: Int,
    val deskripsi_barang: String,
    val stock: Int,
    val gambar: String,
    val lokasi: String
) {

}
