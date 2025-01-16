package com.example.bjbo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.bjbo.databinding.ActivityLaporanBinding
import com.example.bjbo.model.LaporanRequest
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormLaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up spinner for jenis laporan
        val jenisLaporanList = listOf("Penipuan", "Postingan Palsu", "Lainnya")
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            jenisLaporanList
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenisLaporan.adapter = spinnerAdapter

        // Handle submit button
        binding.btnKirimLaporan.setOnClickListener {
            val jenisLaporan = binding.spinnerJenisLaporan.selectedItem.toString()
            val teksLaporan = binding.etTeksLaporan.text.toString().trim()

            if (teksLaporan.isEmpty()) {
                Toast.makeText(this, "Teks laporan tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim laporan ke server
            val postinganId = intent.getIntExtra("postingan_id", -1)
            val userId = intent.getIntExtra("user_id", -1)

            if (postinganId != -1 && userId != -1) {
                val laporanRequest = LaporanRequest(
                    jenis_laporan = jenisLaporan,
                    teks_laporan = teksLaporan,
                    user_id = userId,
                    postingan_id = postinganId,
                    status = "Pending"
                )

                kirimLaporan(laporanRequest)
            } else {
                Toast.makeText(this, "Data laporan tidak valid.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun kirimLaporan(laporan: LaporanRequest) {
        ApiClient.instance.addLaporan(laporan).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FormLaporanActivity, "Laporan berhasil dikirim.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@FormLaporanActivity, "Gagal mengirim laporan.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@FormLaporanActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
