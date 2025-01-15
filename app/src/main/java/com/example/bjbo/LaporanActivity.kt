package com.example.bjbo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.databinding.ActivityLaporanBinding
import com.example.bjbo.model.Laporan
import com.example.bjbo.network.LaporanApiClient

import com.example.bjbo.ui.LaporanAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding
    private val laporanList = mutableListOf<Laporan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewLaporan.layoutManager = LinearLayoutManager(this)
        val adapter = LaporanAdapter(this, laporanList)
        binding.recyclerViewLaporan.adapter = adapter

        loadLaporans(adapter)

        // Tombol tambah laporan
        binding.btnTambahLaporan.setOnClickListener {
            val jenisLaporan = binding.etJenisLaporan.text.toString()
            val teksLaporan = binding.etTeksLaporan.text.toString()
            if (jenisLaporan.isEmpty() || teksLaporan.isEmpty()) {
                showToast("Harap isi semua field")
            } else {
                val laporan = Laporan(
                    id = 0, // ID akan diisi oleh server
                    jenis_laporan = jenisLaporan,
                    teks_laporan = teksLaporan,
                    user_id = 1, // Ubah sesuai user login
                    status = "Pending",
                    postingan_id = 1, // Ubah sesuai postingan yang dilaporkan
                    created_at = "",
                    updated_at = ""
                )
                addLaporan(laporan, adapter)
            }
        }
    }

    private fun loadLaporans(adapter: LaporanAdapter) {
        LaporanApiClient.instance.getAllLaporans().enqueue(object : Callback<List<Laporan>> {
            override fun onResponse(call: Call<List<Laporan>>, response: Response<List<Laporan>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        laporanList.clear()
                        laporanList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showToast("Gagal memuat laporan: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Laporan>>, t: Throwable) {
                showToast("Terjadi kesalahan: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun addLaporan(laporan: Laporan, adapter: LaporanAdapter) {
        LaporanApiClient.instance.addLaporan(laporan).enqueue(object : Callback<Laporan> {
            override fun onResponse(call: Call<Laporan>, response: Response<Laporan>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        laporanList.add(it)
                        adapter.notifyDataSetChanged()
                        showToast("Laporan berhasil ditambahkan")
                    }
                } else {
                    showToast("Gagal menambahkan laporan: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Laporan>, t: Throwable) {
                showToast("Terjadi kesalahan: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
