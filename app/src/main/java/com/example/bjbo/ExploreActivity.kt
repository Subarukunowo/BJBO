package com.example.bjbo

import PostinganListFragment
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.bjbo.databinding.ActivityExploreBinding

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
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_explore -> true
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // Tambahkan fragment PostinganFragmentVertical
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.fragmentContainerVertical, // ID sesuai dengan layout XML
                  PostinganListFragment()
                )
            }
        }
    }
}
