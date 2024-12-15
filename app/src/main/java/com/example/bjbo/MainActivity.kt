package com.example.app

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Animasi gelombang

        // Menghubungkan layout dengan ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur ViewModel ke Binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            viewModel.validateCredentials(this)
        }

        viewModel.loginStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Login gagal! Email atau Password salah.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
