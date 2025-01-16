package com.example.bjbo.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id") val id: Long,                       // ID transaksi
    @SerializedName("user_id") val userId: Long,              // ID pengguna
    @SerializedName("order_id") val orderId: String,          // Order ID unik
    @SerializedName("payment_type") val paymentType: String,  // Jenis pembayaran (e.g., bank_transfer, e-wallet)
    @SerializedName("transaction_status") val transactionStatus: String, // Status transaksi (e.g., pending, success)
    @SerializedName("gross_amount") val grossAmount: Double,  // Total jumlah transaksi
    @SerializedName("payment_code") val paymentCode: String?, // Kode pembayaran (opsional)
    @SerializedName("pdf_url") val pdfUrl: String?,           // URL PDF bukti pembayaran (opsional)
    @SerializedName("created_at") val createdAt: String,      // Tanggal dan waktu transaksi dibuat
    @SerializedName("updated_at") val updatedAt: String       // Tanggal dan waktu transaksi diperbarui
)
