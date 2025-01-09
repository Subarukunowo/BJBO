package com.example.bjbo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityPembayaranBinding
import com.example.bjbo.model.Barang
import com.example.bjbo.model.Pembayaran
import com.example.bjbo.network.RetrofitInstance
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.internal.network.model.response.SnapTokenResponse
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPembayaranBinding
    private lateinit var barangDipilih: Barang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil ID barang dari Intent
        val idBarang = intent.getStringExtra("ID_BARANG") ?: ""

        // Ambil data barang dari API
        getBarangFromApi(idBarang)
    }

    private fun getBarangFromApi(idBarang: String) {
        RetrofitInstance.api.getBarang().enqueue(object : Callback<List<Barang>> {
            override fun onResponse(call: Call<List<Barang>>, response: Response<List<Barang>>) {
                if (response.isSuccessful) {
                    val barangList = response.body() ?: emptyList()
                    barangDipilih = barangList.firstOrNull { it.id_barang == idBarang }
                        ?: Barang("", "Barang Tidak Ditemukan", "", 0, "", 0, "", "")

                    // Update UI
                    updateUI()
                } else {
                    Toast.makeText(
                        this@PembayaranActivity,
                        "Gagal memuat data barang",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>, t: Throwable) {
                Toast.makeText(this@PembayaranActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun updateUI() {
        // Tampilkan hanya id_barang dan harga di UI
        binding.tvNamaBarang.text = "ID Barang: ${barangDipilih.id_barang}"
        binding.tvHargaProduk.text = "Harga: Rp ${barangDipilih.harga}"

        // Tombol bayar
        binding.btnBayar.setOnClickListener {
            fetchSnapTokenAndStartPayment(barangDipilih)
        }
    }

    private fun fetchSnapTokenAndStartPayment(barang: Barang) {
        // Contoh payload untuk mendapatkan Snap Token
        val requestPayload = mapOf(
            "transaction_details" to mapOf(
                "order_id" to "ORDER-${System.currentTimeMillis()}",
                "gross_amount" to barang.harga
            ),
            "item_details" to listOf(
                mapOf(
                    "id" to barang.id_barang,
                    "price" to barang.harga,
                    "quantity" to 1,
                    "name" to barang.nama_barang
                )
            ),
            "customer_details" to mapOf(
                "first_name" to "Customer",
                "email" to "customer@example.com"
            )
        )
    }
}


/*
        // Kirim permintaan ke backend (RetrofitInstance API)
        RetrofitInstance.api.createSnapToken(requestPayload).enqueue(object : Callback<SnapTokenResponse> {
            override fun onResponse(call: Call<SnapTokenResponse>, response: Response<SnapTokenResponse>) {
                if (response.isSuccessful) {
                    val snapToken = response.body()?.token
                    if (!snapToken.isNullOrEmpty()) {
                        startPaymentWithSnapToken(snapToken, barang)
                    } else {
                        Toast.makeText(this@PembayaranActivity, "Snap Token kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PembayaranActivity, "Gagal mendapatkan Snap Token", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SnapTokenResponse>, t: Throwable) {
                Toast.makeText(this@PembayaranActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startPaymentWithSnapToken(snapToken: String, barang: Barang) {
        // Memulai pembayaran Midtrans dengan Snap Token
        MidtransSDK.getInstance().startPaymentUiFlow(this, snapToken)
        MidtransSDK.getInstance().setTransactionFinishedCallback { result ->
            onTransactionFinished(result, barang)
        }
    }

    private fun onTransactionFinished(result: TransactionResult?, barang: Barang) {
        if (result != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    Toast.makeText(this, "Transaksi berhasil!", Toast.LENGTH_LONG).show()
                    handleTransactionResult("Sukses", barang)
                }
                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(this, "Transaksi tertunda!", Toast.LENGTH_LONG).show()
                }
                TransactionResult.STATUS_FAILED -> {
                    Toast.makeText(this, "Transaksi gagal!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Transaksi dibatalkan!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "Transaksi tidak selesai!", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleTransactionResult(status: String, barang: Barang) {
        // Tampilkan hasil transaksi
        val message = """
            Status: $status
            ID Barang: ${barang.id_barang}
            Nama Barang: ${barang.nama_barang}
        """.trimIndent()

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        // Anda bisa menavigasi ke halaman konfirmasi di sini
    }
}
*/