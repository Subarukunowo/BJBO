package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.UserDBHelper
import com.example.bjbo.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDBHelper(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi email dan password.", Toast.LENGTH_SHORT).show()
            } else {
                // Periksa login
                val isValid = dbHelper.login(email, password)
                if (isValid) {
                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToHome(email)
                } else {
                    Toast.makeText(this, "Email atau password salah.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerText.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToHome(email: String) {
        val intent = Intent(this, BerandaActivity::class.java).apply {
            putExtra("USER_EMAIL", email)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
