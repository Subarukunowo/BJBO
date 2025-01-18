package com.example.bjbo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityAccountBinding
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.User
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memuat data profil pengguna
        loadUserProfile()

        // Tombol simpan
        binding.btnSave.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        val userId = getUserIdFromPreferences()
        if (userId == -1) {
            showToast("User ID tidak ditemukan. Harap login ulang.")
            return
        }

        ApiClient.instance.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    currentUser = response.body()
                    populateUserProfile(currentUser)
                } else {
                    showToast("Gagal memuat data profil.")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }

    private fun populateUserProfile(user: User?) {
        user?.let {
            binding.apply {
                tvFirstName.text = it.name ?: "Tidak tersedia"
                tvEmailField.text = it.email ?: "Tidak tersedia"
                tvGender.text = it.kelamin ?: "Tidak tersedia"
                tvPhone.text = it.nomor_hp ?: "Tidak tersedia"
                tvAddress.text = it.alamat ?: "Tidak tersedia"
            }
        }
    }

    private fun saveUserProfile() {
        if (currentUser == null) {
            showToast("Gagal menyimpan profil. Data pengguna tidak valid.")
            return
        }

        // Data dari UI
        val profileRequest = ProfileRequest(
            name = binding.tvFirstName.text.toString(),
            email = binding.tvEmailField.text.toString(),
            nomor_hp = binding.tvPhone.text.toString(),
            kelamin = binding.tvGender.text.toString(),
            alamat = binding.tvAddress.text.toString(),
            profile_picture = currentUser?.profile_picture // Tetap gunakan gambar yang lama jika tidak diubah
        )

        // Panggil API
        currentUser?.id?.let { userId ->
            ApiClient.instance.updateUserProfile(userId, profileRequest).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        showToast("Profil berhasil diperbarui")
                        loadUserProfile() // Memuat ulang profil
                    } else {
                        showToast("Gagal memperbarui profil. Kode: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    showToast("Kesalahan jaringan: ${t.message}")
                }
            })
        }
    }

    private fun getUserIdFromPreferences(): Int {
        // Implementasi untuk mengambil User ID dari SharedPreferences
        return 1 // Contoh, ubah sesuai implementasi nyata
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
