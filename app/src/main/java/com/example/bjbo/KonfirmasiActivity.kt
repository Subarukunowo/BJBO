package com.example.bjbo


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityKonfirmasiBinding



class KonfirmasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKonfirmasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonfirmasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val namaBarang = intent.getStringExtra("NAMA_BARANG") ?: ""
        val totalPembayaran = intent.getIntExtra("TOTAL_PEMBAYARAN", 0)
        val metodePembayaran = intent.getStringExtra("METODE_PEMBAYARAN") ?: ""
        val snapToken = intent.getStringExtra("SNAP_TOKEN") ?: ""

        // Set data ke UI
        binding.tvNamaBarang.text = namaBarang
        binding.tvMetodePembayaran.text = metodePembayaran
        binding.tvTotal.text = "Rp $totalPembayaran"

        if (metodePembayaran == "E-Wallet" && snapToken.isNotEmpty()) {
            // Jika metode pembayaran adalah E-Wallet, tampilkan Snap Token
            binding.tvTrackingId.text = "Snap Token: $snapToken"
            binding.tvOrderId.text = "Menggunakan E-Wallet"
        } else {
            // Jika metode pembayaran COD, tampilkan status sesuai
            binding.tvTrackingId.text = "Tracking ID: 123456789"
            binding.tvOrderId.text = "Metode COD - Sedang diproses"
        }

        // Tombol Konfirmasi
        binding.btnKonfirmasi.setOnClickListener {
            if (metodePembayaran == "E-Wallet" && snapToken.isNotEmpty()) {
                // Logika untuk memproses jika menggunakan E-Wallet
                Toast.makeText(
                    this,
                    "Transaksi melalui E-Wallet berhasil diproses!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Logika untuk memproses jika menggunakan COD
                Toast.makeText(
                    this,
                    "Pesanan dengan metode COD sedang diproses!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish() // Kembali ke activity sebelumnya
        }
    }
}
