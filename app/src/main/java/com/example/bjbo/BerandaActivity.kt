package com.example.bjbo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.api.ApiClient
import com.example.bjbo.databinding.ActivityBerandaBinding
import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding
    private lateinit var barangAdapter: BarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchBarangData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        barangAdapter = BarangAdapter(emptyList())
        binding.recyclerView.adapter = barangAdapter
    }

    private fun fetchBarangData() {
        ApiClient.apiService.getBarang().enqueue(object : Callback<List<Barang>> {
            override fun onResponse(call: Call<List<Barang>>, response: Response<List<Barang>>) {
                if (response.isSuccessful) {
                    val barangList = response.body() ?: emptyList()
                    barangAdapter.updateData(barangList)
                } else {
                    Toast.makeText(this@BerandaActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>, t: Throwable) {
                Toast.makeText(this@BerandaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
