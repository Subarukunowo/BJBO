package com.example.bjbo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.databinding.ActivityUlasanBinding
import com.example.bjbo.model.UlasanRequest
import com.example.bjbo.model.ApiResponse
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UlasanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUlasanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUlasanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ratingBar = binding.rbUserRating
        val reviewEditText = binding.etUserReview
        val sendButton = binding.btnSendReview

        sendButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val komentar = reviewEditText.text.toString()

            // Validasi input pengguna
            if (rating == 0) {
                showToast("Silakan beri rating!")
                return@setOnClickListener
            }

            if (komentar.isBlank()) {
                showToast("Tulis ulasan Anda!")
                return@setOnClickListener
            }

            // Kirim ulasan ke server
            submitReview(rating, komentar)
        }
    }

    private fun submitReview(rating: Int, komentar: String) {
        // Ambil data user_id dan postingan_id dari SharedPreferences
        val userId = SharedPreferencesHelper.getUserId(this)
        val postinganId = SharedPreferencesHelper.getPostinganId(this)

        // Buat model UlasanRequest
        val ulasanRequest = UlasanRequest(
            postingan_id = postinganId,
            user_id = userId,
            rating = rating,
            komentar = komentar
        )

        // Kirim ulasan melalui API
        ApiClient.instance.addUlasan(ulasanRequest).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        showToast("Ulasan berhasil disimpan!")
                        finish() // Kembali ke halaman sebelumnya
                    } else {
                        showToast("Gagal menyimpan ulasan: ${apiResponse?.message}")
                    }
                } else {
                    showToast("Gagal menyimpan ulasan: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                showToast("Kesalahan: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
