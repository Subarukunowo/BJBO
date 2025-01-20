package com.example.bjbo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityAccountBinding
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.User
import com.example.bjbo.network.ApiClient
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.model.ApiResponse
import com.example.bjbo.model.UserRespons
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

        ApiClient.instance.getUserById(userId).enqueue(object : Callback<UserRespons> {
            override fun onResponse(call: Call<UserRespons>, response: Response<UserRespons>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.isSuccessful()) {
                        val user = userResponse.data
                        Log.d("AccountActivity", "Data user diterima: $user")
                        currentUser = user
                        populateUserProfile(user)
                    } else {
                        showToast(userResponse?.message ?: "Gagal memuat data profil.")
                    }
                } else {
                    showToast("Gagal memuat data profil. Kode: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRespons>, t: Throwable) {
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }


    private fun populateUserProfile(user: User?) {
        user?.let {
            binding.apply {
                tvName.text = it.name ?: "Tidak tersedia"
                tvEmail.text = it.email ?: "Tidak tersedia"
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

        val profileRequest = ProfileRequest(
            name = binding.tvFirstName.text.toString(),
            email = binding.tvEmailField.text.toString(),
            nomor_hp = binding.tvPhone.text.toString(),
            kelamin = binding.tvGender.text.toString(),
            alamat = binding.tvAddress.text.toString(),
            profile_picture = currentUser?.profile_picture
        )

        val userId = getUserIdFromPreferences()
        if (userId == -1) {
            showToast("User ID tidak ditemukan. Harap login ulang.")
            return
        }

        ApiClient.instance.updateUserProfile(userId, profileRequest).enqueue(object : Callback<UserRespons> {
            override fun onResponse(call: Call<UserRespons>, response: Response<UserRespons>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.isSuccessful()) {
                        showToast("Profil berhasil diperbarui")
                        loadUserProfile()
                    } else {
                        showToast(userResponse?.message ?: "Gagal memperbarui profil.")
                    }
                } else {
                    showToast("Gagal memperbarui profil. Kode: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRespons>, t: Throwable) {
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }

    private fun getUserIdFromPreferences(): Int {
        return SharedPreferencesHelper.getUserId(this)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
