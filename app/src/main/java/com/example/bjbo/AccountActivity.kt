package com.example.bjbo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.UserDBHelper
import com.example.bjbo.databinding.ActivityAccountBinding
import com.example.bjbo.model.User

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var dbHelper: UserDBHelper
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDBHelper(this)

        // Load user data
        val userEmail = intent.getStringExtra("email") ?: "default@example.com"
        loadUserDetails(userEmail)

        // Save button action
        binding.btnSave.setOnClickListener {
            saveUserDetails()
        }

        // Change profile picture action
        binding.ivProfilePicture.setOnClickListener {
            pickImage()
        }
    }

    private fun loadUserDetails(identifier: String) {
        val user = dbHelper.getUser(identifier)
        if (user != null) {
            currentUser = user
            binding.tvFirstName.text = user.nama_depan.ifEmpty { "Nama Depan" }
            binding.tvLastName.text = user.nama_belakang.ifEmpty { "Nama Belakang" }
            binding.tvEmailField.text = user.email
            binding.tvPhone.text = user.nomor_hp.ifEmpty { "Nomor HP" }
            binding.tvAddress.text = "Alamat belum diatur."
        } else {
            // Default user if not found
            currentUser = User(
                id_user = identifier,
                username = "",
                password = "",
                nama_depan = "",
                nama_belakang = "",
                email = identifier,
                nomor_hp = "",
                foto_profil = null
            )
            binding.tvFirstName.text = "Nama Depan"
            binding.tvLastName.text = "Nama Belakang"
            binding.tvEmailField.text = identifier
            binding.tvPhone.text = "Nomor HP"
            binding.tvAddress.text = "Alamat belum diatur."
        }
    }

    private fun saveUserDetails() {
        val firstName = binding.tvFirstName.text.toString().trim()
        val lastName = binding.tvLastName.text.toString().trim()
        val phone = binding.tvPhone.text.toString().trim()

        val updatedUser = currentUser.copy(
            nama_depan = firstName,
            nama_belakang = lastName,
            nomor_hp = phone
        )

        val isUpdated = dbHelper.updateUser(updatedUser)

        if (isUpdated) {
            Toast.makeText(this, "Data berhasil diperbarui.", Toast.LENGTH_SHORT).show()
            currentUser = updatedUser
        } else {
            Toast.makeText(this, "Gagal memperbarui data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                binding.ivProfilePicture.setImageURI(imageUri)

                val updatedUser = currentUser.copy(foto_profil = imageUri.toString())
                if (dbHelper.updateUser(updatedUser)) {
                    currentUser = updatedUser
                    Toast.makeText(this, "Gambar profil berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal memperbarui gambar profil.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
