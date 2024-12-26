package com.example.bjbo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bjbo.api.ApiClient
import com.example.bjbo.databinding.ActivityDetailProdukBinding
import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProdukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil ID produk dari intent
        val productId = intent.getStringExtra("PRODUCT_ID")
        if (productId != null) {
            fetchProductDetail(productId)
        } else {
            Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Back button listener
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchProductDetail(productId: String) {
        ApiClient.instance.getBarangDetail(productId).enqueue(object : Callback<Barang> {
            override fun onResponse(call: Call<Barang>, response: Response<Barang>) {
                if (response.isSuccessful) {
                    response.body()?.let { barang ->
                        bindProductDetail(barang)
                    }
                } else {
                    Toast.makeText(this@DetailProductActivity, "Gagal memuat detail produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Barang>, t: Throwable) {
                Toast.makeText(this@DetailProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindProductDetail(barang: Barang) {
        binding.apply {
            tvProductTitle.text = barang.nama_barang
            tvSellerName.text = "Penjual: ${barang.kategori}"
            tvProductPrice.text = "Rp ${barang.harga}"
            tvProductDescription.text = barang.deskripsi_barang
            tvProductCondition.text = "Stok: ${barang.stock}"

            // Load gambar menggunakan Glide
            Glide.with(this@DetailProductActivity)
                .load(barang.gambar)
                .into(ivProductImage)
        }
    }
}
