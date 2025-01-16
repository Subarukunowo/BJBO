package com.example.bjbo.model


import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("id") val id: Long,                         // ID pembayaran
    @SerializedName("transaction_id") val transactionId: Long,  // ID transaksi
    @SerializedName("payment_type") val paymentType: String,    // Jenis pembayaran (e.g., credit_card, e-wallet)
    @SerializedName("payment_status") val paymentStatus: String,// Status pembayaran (e.g., paid, pending)
    @SerializedName("payment_method") val paymentMethod: String,// Metode pembayaran (e.g., online, offline)
    @SerializedName("amount_paid") val amountPaid: Double,       // Jumlah yang dibayar
    @SerializedName("transaction") val transaction: Transaction?, // Relasi dengan transaksi (opsional)
    @SerializedName("user") val user: User?                     // Relasi dengan pengguna (opsional)
)
