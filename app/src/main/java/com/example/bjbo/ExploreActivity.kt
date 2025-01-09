package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityExploreBinding
import com.example.bjbo.fragment.ProdukBaruFragment
import com.example.bjbo.network.ApiClient
import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menampilkan semua produk secara default
        fetchProdukByKategori("")

        // Listener untuk Chip Kategori
        binding.chipBooks.setOnClickListener { fetchProdukByKategori("Books") }
        binding.chipGames.setOnClickListener { fetchProdukByKategori("Games") }
        binding.chipMusic.setOnClickListener { fetchProdukByKategori("Music") }
        binding.chipCamera.setOnClickListener { fetchProdukByKategori("Camera") }

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
    }

    /**
     * Mengambil produk berdasarkan kategori menggunakan Mock API
     */
    private fun fetchProdukByKategori(kategori: String) {
        ApiClient.instance.getBarang().enqueue(object : Callback<List<Barang>> {
            override fun onResponse(call: Call<List<Barang>>, response: Response<List<Barang>>) {
                if (response.isSuccessful) {
                    val produkList = response.body()?.filter { it.kategori == kategori || kategori.isEmpty() } ?: emptyList()
                    showProdukInFragment(produkList)
                } else {
                    Toast.makeText(this@ExploreActivity, "Gagal memuat produk.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>, t: Throwable) {
                Toast.makeText(this@ExploreActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Menampilkan produk di dalam fragment ProdukBaruFragment
     */
    private fun showProdukInFragment(produkList: List<Barang>) {
        val fragment = ProdukBaruFragment.newInstance(produkList.toString())
        supportFragmentManager.beginTransaction()
            .replace(binding.produkBaruFragmentContainer.id, fragment)
            .commit()
    }
}
