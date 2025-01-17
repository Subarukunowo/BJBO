package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.databinding.ActivityMainBinding
import com.example.bjbo.model.LoginRequest
import com.example.bjbo.model.User
import com.example.bjbo.model.UserRespons
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periksa apakah pengguna sudah login
        if (SharedPreferencesHelper.isUserLoggedIn(this)) {
            navigateToHome()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi email dan password.", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        binding.registerText.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        ApiClient.instance.loginUser(loginRequest).enqueue(object : Callback<UserRespons> {
            override fun onResponse(call: Call<UserRespons>, response: Response<UserRespons>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body() // Get the ApiResponse object
                    if (apiResponse != null && apiResponse.success) {
                        val user = apiResponse.data // Extract the User from the ApiResponse
                        if (user != null) {
                            // Log data pengguna yang berhasil login
                            Log.d("LoginActivity", "Login Berhasil: ID=${user.id}, Name=${user.name}")

                            // Simpan id dan name ke SharedPreferences
                            saveUserToSharedPreferences(user.id ?: 0, user.name ?: "Pengguna")

                            // Tampilkan notifikasi login berhasil
                            Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                            // Navigasi ke beranda
                            navigateToHome()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login gagal. Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Login gagal: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Login gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserRespons>, t: Throwable) {
                Log.e("LoginActivity", "Terjadi kesalahan: ${t.localizedMessage}")
                Toast.makeText(this@LoginActivity, "Terjadi kesalahan: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserToSharedPreferences(userId: Int, userName: String) {
        SharedPreferencesHelper.saveUserPreferences(this, userId, userName)
        Log.d("LoginActivity", "Data tersimpan di SharedPreferences: ID=$userId, Name=$userName")
    }

    private fun navigateToHome() {
        val intent = Intent(this, BerandaActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
