package com.example.bjbo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityAccountBinding
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.User
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var currentUser: User
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Spinner Data
        val kelaminAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Laki-laki", "Perempuan")
        )
        kelaminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKelamin.adapter = kelaminAdapter

        // Load User Profile
        loadUserProfile()

        // Change profile picture
        binding.ivProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Save Profile
        binding.btnSave.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        val userId = getUserIdFromPreferences() // Get user ID from preferences
        if (userId == -1) {
            Toast.makeText(this, "User ID tidak ditemukan. Harap login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Log userId
        Log.d("AccountActivity", "loadUserProfile: User ID = $userId")

        // Make API call to get user profile
        ApiClient.instance.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("AccountActivity", "loadUserProfile: Response code = ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { user ->
                        currentUser = user

                        // Log the received user data
                        Log.d("AccountActivity", "loadUserProfile: User data = $user")

                        // Update UI fields with user data
                        binding.apply {
                            tvName.setText(user.name)
                            tvEmailField.setText(user.email)
                            tvPhone.setText(user.alamat)

                            // Set spinner selection based on gender
                            val genderPosition = when (user.kelamin?.lowercase()) {
                                "laki-laki" -> 0
                                "perempuan" -> 1
                                else -> 0 // Default to first position
                            }
                            spinnerKelamin.setSelection(genderPosition)
                        }
                    }
                } else {
                    // Log error response
                    Log.e("AccountActivity", "loadUserProfile: Error response = ${response.errorBody()?.string()}")
                    val errorMessage = when (response.code()) {
                        404 -> "Pengguna tidak ditemukan"
                        401 -> "Tidak memiliki akses"
                        else -> "Gagal memuat profil (${response.code()})"
                    }
                    Toast.makeText(this@AccountActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Log network failure
                Log.e("AccountActivity", "loadUserProfile: Network error = ${t.message}", t)
                Toast.makeText(this@AccountActivity, "Terjadi kesalahan jaringan.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserProfile() {
        // Validate required fields first
        if (binding.tvName.text.isNullOrEmpty() ||
            binding.tvEmailField.text.isNullOrEmpty() ||
            binding.tvPhone.text.isNullOrEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Log UI data before sending the request
        Log.d("AccountActivity", "saveUserProfile: UI Data = Name: ${binding.tvName.text}, Email: ${binding.tvEmailField.text}, Alamat: ${binding.tvPhone.text}")

        // Create ProfileRequest object from UI fields
        val profileRequest = ProfileRequest(
            name = binding.tvName.text.toString(),
            email = binding.tvEmailField.text.toString(),
            alamat = binding.tvPhone.text.toString(),
            kelamin = binding.spinnerKelamin.selectedItem.toString(),
            profile_picture = selectedImageUri?.toString() // Convert Uri to string path
        )

        // Get user ID from preferences
        val userId = getUserIdFromPreferences()
        Log.d("AccountActivity", "saveUserProfile: User ID = $userId")

        // Make API call to update profile
        ApiClient.instance.updateUserProfile(userId, profileRequest).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("AccountActivity", "saveUserProfile: Response code = ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("AccountActivity", "saveUserProfile: Updated user data = ${response.body()}")
                    Toast.makeText(this@AccountActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    // Reload user profile to show updated data
                    loadUserProfile()
                } else {
                    Log.e("AccountActivity", "saveUserProfile: Error response = ${response.errorBody()?.string()}")
                    Toast.makeText(this@AccountActivity, "Gagal memperbarui profil: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Log network failure
                Log.e("AccountActivity", "saveUserProfile: Network error = ${t.message}", t)
                Toast.makeText(this@AccountActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.ivProfilePicture.setImageURI(selectedImageUri)
        }
    }

    private fun getUserIdFromPreferences(): Int {
        // Implementasi untuk mendapatkan user ID dari SharedPreferences
        return 1 // Contoh: Ganti dengan user ID aktual
    }
}
