package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bjbo.databinding.ActivityDetailPostinganBinding
import java.text.NumberFormat
import java.util.Locale

class DetailPostinganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostinganBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostinganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val name = intent.getStringExtra("name") ?: "Unknown"
        val priceString = intent.getStringExtra("price") ?: "0"
        val price = try {
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).parse(priceString)?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
        val location = intent.getStringExtra("location") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description"
        val image = intent.getStringExtra("image") ?: ""

        // Set data ke UI
        binding.tvPostinganName.text = name
        binding.tvPostinganPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price)
        binding.tvPostinganLocation.text = location
        binding.tvPostinganDescription.text = description

        // Load image menggunakan Glide
        Glide.with(this)
            .load(image) // URL gambar dari API
            .into(binding.ivPostinganImage)

        // Tombol Beli
        binding.btnBuy.setOnClickListener {
            if (price > 0L) {
                navigateToPembayaranActivity(name, price, location, description, image)
            } else {
                showToast("Harga tidak valid")
            }
        }
    }

    private fun navigateToPembayaranActivity(
        name: String,
        price: Long,
        location: String,
        description: String,
        image: String
    ) {
        val intent = Intent(this, PembayaranActivity::class.java).apply {
            putExtra("name", name)
            putExtra("price", price)
            putExtra("location", location)
            putExtra("description", description)
            putExtra("image", image)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
