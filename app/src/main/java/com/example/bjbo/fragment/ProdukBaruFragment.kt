package com.example.bjbo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.DetailProductActivity
import com.example.bjbo.databinding.FragmentProdukBaruBinding
import com.example.bjbo.model.Barang
import com.example.bjbo.network.ApiClient
import com.example.bjbo.ui.BarangbiasaAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukBaruFragment : Fragment() {

    private lateinit var binding: FragmentProdukBaruBinding
    private lateinit var barangAdapter: BarangbiasaAdapter

    companion object {
        private const val ARG_KATEGORI = "KATEGORI"

        fun newInstance(kategori: String?): ProdukBaruFragment {
            val fragment = ProdukBaruFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KATEGORI, kategori) // Kategori bisa null untuk semua produk
            fragment.arguments = bundle
            return fragment
        }
    }

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

        // Ambil kategori dari arguments
        val kategori = arguments?.getString(ARG_KATEGORI)

        // Fetch data berdasarkan kategori
        fetchProdukByKategori(kategori)
    }

    private fun setupRecyclerView() {
        barangAdapter = BarangbiasaAdapter(emptyList()) { barang ->
            // Klik item untuk membuka DetailProductActivity
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("ID_BARANG", barang.id_barang)
            startActivity(intent)
        }

        binding.recyclerViewProdukBaru.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = barangAdapter
        }
    }

    private fun fetchProdukByKategori(kategori: String?) {
        ApiClient.instance.getBarang().enqueue(object : Callback<List<Barang>> {
            override fun onResponse(call: Call<List<Barang>>, response: Response<List<Barang>>) {
                if (response.isSuccessful) {
                    val produkList = response.body()?.filter {
                        kategori.isNullOrEmpty() || it.kategori.equals(kategori, ignoreCase = true)
                    } ?: emptyList()
                    barangAdapter.updateData(produkList)
                } else {
                    Toast.makeText(context, "Gagal memuat produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
