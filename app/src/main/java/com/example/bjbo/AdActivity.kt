package com.example.bjbo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        // Referensi ke elemen UI
        val adImage = findViewById<ImageView>(R.id.ivAdImage)
        val adTitle = findViewById<TextView>(R.id.tvAdTitle)
        val adDescription = findViewById<TextView>(R.id.tvAdDescription)
        val closeButton = findViewById<Button>(R.id.btnCloseAd)

        // Atur data iklan (1 Iklan)
        adImage.setImageResource(R.drawable.iklan9) // Gambar iklan
        adTitle.text = "Promo Spesial Bulan Ini!" // Judul iklan
        adDescription.text = "Diskon hingga 50% untuk semua produk." // Deskripsi iklan

        // Tombol Tutup
        closeButton.setOnClickListener {
            finish() // Menutup activity
        }
    }
}
