package com.example.bjbo

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityEditProfileBinding
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.UserRespons
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load data dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        val name = sharedPreferences.getString("name", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""

        // Set data awal ke UI
        binding.etName.setText(name)
        binding.etEmail.setText(email)

        // Tombol kembali
        binding.ivBack.setOnClickListener {
            finish()
        }

        // Tombol simpan
        binding.btnSave.setOnClickListener {
            val updatedName = binding.etName.text.toString()
            val updatedEmail = binding.etEmail.text.toString()

            if (userId != -1 && updatedName.isNotEmpty() && updatedEmail.isNotEmpty()) {
                updateUserProfile(userId, updatedName, updatedEmail)
            } else {
                Toast.makeText(this, "Harap isi semua kolom dengan benar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserProfile(userId: Int, name: String, email: String) {
        val profileRequest = ProfileRequest(
            name = name,
            email = email,
            nomor_hp = "",
            alamat = "",
            kelamin = "",
            profile_picture = null
        )

        ApiClient.instance.updateUserProfile(userId, profileRequest)
            .enqueue(object : Callback<UserRespons> {
                override fun onResponse(call: Call<UserRespons>, response: Response<UserRespons>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Simpan perubahan ke SharedPreferences
                        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("name", name)
                        editor.putString("email", email)
                        editor.apply()

                        Toast.makeText(this@EditProfileActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke layar sebelumnya
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Gagal memperbarui profil!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserRespons>, t: Throwable) {
                    Log.e("EditProfileActivity", "Kesalahan jaringan: ${t.message}")
                    Toast.makeText(this@EditProfileActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
