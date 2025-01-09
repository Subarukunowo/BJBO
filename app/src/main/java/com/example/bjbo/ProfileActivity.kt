package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.bjbo.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Klik untuk menutup aktivitas (Close Icon)
        binding.closeIcon.setOnClickListener {
            finish() // Menutup aktivitas
        }



        // Navigasi ke "Akun Saya"
        binding.ivAkun.setOnClickListener {
            Toast.makeText(this, "Menuju Akun Saya", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Akun Saya
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)

        }

        // Navigasi ke "Notifikasi"
        binding.ivNotifikasi.setOnClickListener {
            Toast.makeText(this, "Menuju Notifikasi", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Notifikasi
        }

        // Navigasi ke "Produk Dijual"
        binding.ivProdukDijual.setOnClickListener {
            Toast.makeText(this, "Menuju Produk Dijual", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Produk Dijual
        }

        // Navigasi ke "Produk Disukai"
        binding.ivProdukDisukai.setOnClickListener {
            Toast.makeText(this, "Menuju Produk Disukai", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Produk Disukai
        }

        // Navigasi ke "Orderan Saya"
        binding.ivOrderanSaya.setOnClickListener {
            Toast.makeText(this, "Menuju Orderan Saya", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Orderan Saya
        }

        // Navigasi ke "Transaksi Saya"
        binding.ivTransaksiSaya.setOnClickListener {
            Toast.makeText(this, "Menuju Transaksi Saya", Toast.LENGTH_SHORT).show()
            // Tambahkan intent untuk TransaksiActivity
            val intent = Intent(this, TransaksiActivity::class.java)
            startActivity(intent)
        }

        // Tombol Settings
        binding.btnSettings.setOnClickListener {
            Toast.makeText(this, "Menuju Settings", Toast.LENGTH_SHORT).show()
            // Tambahkan intent jika ada activity untuk Settings
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
            // Tambahkan logika logout di sini
        }
    }
}
