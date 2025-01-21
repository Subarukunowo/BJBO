package com.example.bjbo


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.databinding.ActivityDetailPostinganBinding
import com.example.bjbo.fragment.UlasanListFragment
import com.example.bjbo.model.ApiResponse
import com.example.bjbo.model.Favorit

import com.example.bjbo.model.Ulasan
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class DetailPostinganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostinganBinding
    private var isLiked = false
    private val apiService = ApiClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostinganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val name = intent.getStringExtra("name") ?: "Unknown"
        val price = intent.getLongExtra("price", 0L) // Ubah getIntExtra ke getLongExtra
        val location = intent.getStringExtra("location") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description"
        val image = intent.getStringExtra("image") ?: ""
        val postinganId = intent.getIntExtra("postingan_id", -1)

        Log.d("DetailPostinganActivity", "ID postingan diterima: $postinganId")

        // Set data ke UI
        binding.tvPostinganName.text = name
        binding.tvPostinganPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price)
        binding.tvPostinganLocation.text = location
        binding.tvPostinganDescription.text = description

        // Load image menggunakan Glide
        Glide.with(this).load(image).into(binding.ivPostinganImage)

        // Validasi ID postingan
        if (postinganId == -1) {
            Log.e("DetailPostinganActivity", "ID postingan tidak valid!")
            showToast("ID postingan tidak valid.")
            return
        }

        // Muat ulasan berdasarkan postinganId
        loadUlasanFromApi(postinganId)


    binding.etUserReview.setOnClickListener {
            navigateToUlasanActivity()
        }

        // Setup ikon favorit
        val favorit = Favorit(
            id = 1,
            user_id = SharedPreferencesHelper.getUserId(this),
            postingan_id = postinganId,
            created_at = null,
            updated_at = null,
            user = null,
            postingan = null
        )
        setupHeartIcon(binding.ivHeartIcon, favorit)

        // Tombol Beli
        binding.btnBuy.setOnClickListener {
            if (price > 0L) {
                navigateToPembayaranActivity(name, price, location, description, image)
            } else {
                showToast("Harga tidak valid")
            }
        }
    }

    private fun setupHeartIcon(heartIcon: ImageView, favorit: Favorit) {
        isLiked = SharedPreferencesHelper.isFavoriteId(this, favorit.postingan_id)
        heartIcon.setImageResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart)

        heartIcon.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.anim_heart_scale)
            heartIcon.startAnimation(animation)

            if (isLiked) {
                heartIcon.setImageResource(R.drawable.ic_heart)
                removeFromFavorites(favorit)
            } else {
                heartIcon.setImageResource(R.drawable.ic_heart_filled)
                addToFavorites(favorit)
            }
            isLiked = !isLiked
        }
    }

    private fun removeFromFavorites(favorit: Favorit) {
        apiService.deleteFavorit(favorit.id.toLong()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    SharedPreferencesHelper.removeFavoriteId(this@DetailPostinganActivity, favorit.postingan_id)
                    showToast("Berhasil menghapus dari favorit.")
                } else {
                    showToast("Gagal menghapus dari favorit.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }

    private fun addToFavorites(favorit: Favorit) {
        apiService.createFavorit(favorit).enqueue(object : Callback<Favorit> {
            override fun onResponse(call: Call<Favorit>, response: Response<Favorit>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        SharedPreferencesHelper.addFavoriteId(this@DetailPostinganActivity, it.postingan_id)
                        showToast("Berhasil ditambahkan ke favorit.")
                    }
                } else {
                    showToast("Gagal menambahkan ke favorit.")
                }
            }

            override fun onFailure(call: Call<Favorit>, t: Throwable) {
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }

    private fun loadUlasanFromApi(postinganId: Int) {
        apiService.getUlasansByPostinganId(postinganId).enqueue(object : Callback<ApiResponse<List<Ulasan>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Ulasan>>>,
                response: Response<ApiResponse<List<Ulasan>>>
            ) {
                if (response.isSuccessful) {
                    val ulasans = response.body()?.data
                    if (!ulasans.isNullOrEmpty()) {
                        Log.d("LoadUlasanFromApi", "Berhasil memuat ulasan: $ulasans")

                        // Tampilkan ulasan di fragment
                        val fragment = UlasanListFragment.newInstance(postinganId)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.ulasanListFragmentContainer, fragment)
                            .commit()
                    } else {
                        Log.w("LoadUlasanFromApi", "Tidak ada ulasan untuk postingan ini.")
                        showToast("Belum ada ulasan untuk postingan ini.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoadUlasanFromApi", "Gagal memuat ulasan: ${response.code()} - $errorBody")
                    showToast("Gagal memuat ulasan: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Ulasan>>>, t: Throwable) {
                Log.e("LoadUlasanFromApi", "Kesalahan jaringan: ${t.message}", t)
                showToast("Kesalahan jaringan: ${t.message}")
            }
        })
    }


    private fun navigateToPembayaranActivity(
        name: String,
        price: Long,
        location: String,
        description: String,
        image: String
    ) {
        val intent = Intent(this, PembayaranActivity::class.java).apply {
            putExtra("name", name)
            putExtra("price", price) // Kirim harga sebagai Int
            putExtra("location", location)
            putExtra("description", description)
            putExtra("image", image)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUlasanActivity() {
        val intent = Intent(this, UlasanActivity::class.java)
        startActivity(intent)
    }
}
