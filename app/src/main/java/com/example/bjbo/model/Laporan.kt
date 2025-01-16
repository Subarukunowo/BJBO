package com.example.bjbo.model

data class Laporan(
    val id: Int,                 // ID laporan
    val jenis_laporan: String,   // Jenis laporan
    val teks_laporan: String,    // Isi teks laporan
    val user_id: Int,            // ID pengguna pelapor
    val status: String,          // Status laporan (Pending, Selesai, dll.)
    val postingan_id: Int,       // ID postingan yang dilaporkan
    val created_at: String,      // Tanggal laporan dibuat
    val updated_at: String       // Tanggal laporan diperbarui
)
data class LaporanRequest(
    val jenis_laporan: String,
    val teks_laporan: String,
    val user_id: Int,
    val postingan_id: Int,
    val status: String = "Pending"
)