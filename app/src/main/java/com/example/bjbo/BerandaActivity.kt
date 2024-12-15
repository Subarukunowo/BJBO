package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityBerandaBinding

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeText.text = "Selamat datang di Beranda, sandybks7!"
    }
}
