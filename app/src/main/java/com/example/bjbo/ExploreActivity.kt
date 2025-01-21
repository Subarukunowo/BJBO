package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.bjbo.databinding.ActivityExploreBinding
import com.example.bjbo.fragment.ExplorePostinganFragment

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menyetel item yang aktif di BottomNavigationView
        binding.bottomNavigation.selectedItemId = R.id.nav_explore

        // Bottom Navigation Listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.nav_explore -> true
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }

        // Menambahkan fragment ExplorePostinganFragment ke container baru
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.exploreFragmentContainer, // ID yang sesuai dengan layout baru
                    ExplorePostinganFragment() // Menggunakan fragment Explore
                )
            }
        }
    }
}
