package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

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

        // Tambahkan user 'sandybks7' dengan password '12345678' jika belum ada
        val dbHelper = DBHelper(this)
        if (!dbHelper.checkLogin("sandybks7", "12345678")) {
            dbHelper.addUser("sandybks7", "12345678")
        }

        // Observasi status login
        viewModel.loginStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                navigateToBeranda()
            } else {
                Toast.makeText(this, "Login gagal! Email atau password salah.", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol untuk login
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi email dan password.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.email.value = email
                viewModel.password.value = password
                viewModel.validateCredentials(this)
            }
        }
    }

    private fun navigateToBeranda() {
        val intent = Intent(this, BerandaActivity::class.java)
        startActivity(intent)
        finish() // Tutup LoginActivity agar tidak dapat kembali
    }
}
