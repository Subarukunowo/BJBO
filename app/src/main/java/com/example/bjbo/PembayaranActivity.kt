package com.example.bjbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
        // Mendapatkan data produk dari Intent
        val name = intent.getStringExtra("name") ?: "Unknown"
        val price = intent.getLongExtra("price", 0L)
        val biayaAplikasi = 500L
        val biayaTambahan = 500L
        val totalHarga = price + biayaAplikasi + biayaTambahan
        // Menampilkan data produk di UI
        binding.tvNamaBarang.text = name
        binding.tvHargaProduk.text = "Rp $price"
        binding.tvBiayaAplikasi.text = "Rp $biayaAplikasi"
        binding.tvBiayaTambahan.text = "Rp $biayaTambahan"
        binding.tvTotal.text = "Rp $totalHarga"


        setupListeners(totalHarga)
    }

    private fun setupListeners(price: Long) {
        // Listener untuk metode pembayaran
        binding.radioGroupMetodePembayaran.setOnCheckedChangeListener { _, checkedId ->
            selectedMetodePembayaran = when (checkedId) {
                R.id.radioCod -> "COD"
                R.id.radioEwallet -> "E-wallet"
                else -> ""
            }
        }

        // Listener tombol bayar
        binding.btnBayar.setOnClickListener {
            if (selectedMetodePembayaran.isEmpty()) {
                showToast("Pilih metode pembayaran terlebih dahulu!")
            } else {
                processPayment(price)
            }
        }

        // Listener tombol kembali
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

        ApiClient.instance.createOrder(orderRequest).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    val createdOrder = response.body()
                    if (createdOrder != null) {
                        val snapToken = createdOrder.snap_token
                        if (!snapToken.isNullOrEmpty()) {
                            val redirectUrl = "https://app.sandbox.midtrans.com/snap/v2/vtweb/$snapToken"
                            openMidtransPayment(redirectUrl)
                        } else {
                            showToast("Gagal mendapatkan Snap Token.")
                            // Tambahkan logging
                            Log.e("PembayaranActivity", "Snap Token kosong. Respons: ${response.body()}")
                        }
                    } else {
                        showToast("Respons kosong dari server.")
                        Log.e("PembayaranActivity", "Respons kosong. Respons body: ${response.body()}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    showToast("Gagal memproses pesanan: $errorBody")
                    Log.e("PembayaranActivity", "Gagal memproses pesanan. Kode: ${response.code()}, Error: $errorBody")
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
