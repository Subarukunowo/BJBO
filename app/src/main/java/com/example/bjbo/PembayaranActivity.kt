package com.example.bjbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityPembayaranBinding
import com.example.bjbo.model.Order
import com.example.bjbo.model.OrderRequest
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPembayaranBinding
    private var selectedMetodePembayaran: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        val name = intent.getStringExtra("name") ?: "Unknown"
        val price = intent.getLongExtra("price", 0L)

        binding.tvNamaBarang.text = name
        binding.tvHargaProduk.text = "Rp $price"
        binding.tvTotal.text = "Rp $price"

        setupListeners(price)
    }

    private fun setupListeners(price: Long) {
        binding.radioGroupMetodePembayaran.setOnCheckedChangeListener { _, checkedId ->
            selectedMetodePembayaran = when (checkedId) {
                R.id.radioCod -> "COD"
                R.id.radioEwallet -> "E-wallet"
                else -> ""
            }
        }

        binding.btnBayar.setOnClickListener {
            if (selectedMetodePembayaran.isEmpty()) {
                showToast("Pilih metode pembayaran terlebih dahulu!")
            } else {
                processPayment(price)
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun processPayment(price: Long) {
        when (selectedMetodePembayaran) {
            "COD" -> handleCODPayment()
            "E-wallet" -> createOrder(price)
            else -> showToast("Metode pembayaran tidak valid.")
        }
    }

    private fun handleCODPayment() {
        showToast("Pesanan Anda telah diterima. Bayar saat barang diterima.")
        finish()
    }

    private fun createOrder(price: Long) {
        val orderRequest = OrderRequest(
            user_id = getCurrentUserId(),
            postingan_id = getPostinganId(),
            total_harga = price.toDouble(),
            status = "pending"
        )

        ApiClient.instance.createOrder(orderRequest)
            .enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        val createdOrder = response.body()
                        val redirectUrl = createdOrder?.redirectUrl
                        if (!redirectUrl.isNullOrEmpty()) {
                            openMidtransPayment(redirectUrl)
                        } else {
                            showToast("Gagal mendapatkan URL Redirect.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        showToast("Gagal memproses pesanan: $errorBody")
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    showToast("Terjadi kesalahan: ${t.localizedMessage}")
                }
            })
    }

    private fun openMidtransPayment(redirectUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            showToast("Tidak dapat membuka browser untuk pembayaran.")
        }
    }

    private fun getCurrentUserId(): Int {
        // Simulasi mendapatkan user ID (ganti dengan logika aktual)
        return 1
    }

    private fun getPostinganId(): Int {
        // Simulasi mendapatkan postingan ID (ganti dengan logika aktual)
        return 1
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
