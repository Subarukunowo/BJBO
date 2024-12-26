package com.example.bjbo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.BarangAdapter
import com.example.bjbo.DetailProductActivity
import com.example.bjbo.api.ApiClient
import com.example.bjbo.databinding.FragmentProdukBaruBinding
import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukBaruFragment : Fragment() {

    private lateinit var binding: FragmentProdukBaruBinding
    private lateinit var produkBaruAdapter: BarangAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProdukBaruBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchProdukBaru()
    }

    private fun setupRecyclerView() {
        produkBaruAdapter = BarangAdapter(emptyList()) { barang ->
            // Klik item untuk membuka DetailProductActivity
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", barang.id) // Mengirim ID produk
            startActivity(intent)
        }

        binding.recyclerViewProdukBaru.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = produkBaruAdapter
        }
    }

    private fun fetchProdukBaru() {
        ApiClient.instance.getBarang().enqueue(object : Callback<List<Barang>> {
            override fun onResponse(call: Call<List<Barang>>, response: Response<List<Barang>>) {
                if (response.isSuccessful) {
                    response.body()?.let { produkList ->
                        produkBaruAdapter.updateData(produkList)
                    }
                } else {
                    Toast.makeText(context, "Gagal memuat produk baru", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
