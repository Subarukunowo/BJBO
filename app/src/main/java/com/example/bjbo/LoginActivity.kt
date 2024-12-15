package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.BerandaActivity
import com.example.bjbo.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val dbHelper = DBHelper(this)

        // Menambahkan beberapa pengguna jika belum ada
        addMultipleUsers(dbHelper)
        binding.registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Observasi status login
        viewModel.loginStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                navigateToBeranda()
            } else {
                Toast.makeText(this, "Login gagal! Email atau password salah.", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol login
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi email dan password.", Toast.LENGTH_SHORT).show()
            } else {
                // Validasi kredensial menggunakan DBHelper
                if (dbHelper.checkLogin(email, password)) {
                    Toast.makeText(this, "Login berhasil untuk $email", Toast.LENGTH_SHORT).show()
                    navigateToBeranda()
                } else {
                    Toast.makeText(this, "Login gagal! Email atau password salah.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Menambahkan beberapa pengguna secara default
    private fun addMultipleUsers(dbHelper: DBHelper) {
        val users = listOf(
            Pair("dimas123@gmail", "dimas123"),
            Pair("fikri12", "321fikri"),
            Pair("sandybks7", "12345678")
        )

        for (user in users) {
            if (!dbHelper.checkLogin(user.first, user.second)) {
                val isAdded = dbHelper.addUser(user.first, user.second)
                if (isAdded) {
                    println("DEBUG: ${user.first} berhasil ditambahkan.")
                } else {
                    println("DEBUG: Gagal menambahkan ${user.first}.")
                }
            }
        }
    }

    // Navigasi ke halaman Beranda
    private fun navigateToBeranda() {
        val intent = Intent(this, BerandaActivity::class.java)
        startActivity(intent)
        finish() // Tutup LoginActivity agar tidak dapat kembali
    }
}
