package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.DBHelper
import com.example.bjbo.databinding.ActivityRegisterBinding
import com.example.bjbo.database.UserDBHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDBHelper(this)

        binding.registerButton.setOnClickListener {
            val username = binding.emailInput.text.toString().trim() // Gunakan usernameInput
            val password = binding.passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi username dan password.", Toast.LENGTH_SHORT).show()
            } else {
                // Tambahkan user ke database
                val isAdded = dbHelper.registerUser(username, password)
                if (isAdded) {
                    Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                    dbHelper.logAllUsers()
                    navigateToLogin()
                } else {
                    Toast.makeText(this, "Registrasi gagal. Username mungkin sudah digunakan.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerText.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
