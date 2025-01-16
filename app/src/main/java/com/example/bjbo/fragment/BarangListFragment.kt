package com.example.bjbo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.database.BarangDBHelper
import com.example.bjbo.databinding.FragmentBarangListBinding
import com.example.bjbo.model.Barang
import com.example.bjbo.ui.BarangAdapter

class BarangListFragment : Fragment() {

    private lateinit var binding: FragmentBarangListBinding
    private lateinit var dbHelper: BarangDBHelper
    private lateinit var barangAdapter: BarangAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBarangListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi database helper
        dbHelper = BarangDBHelper(requireContext())

        // Setup RecyclerView
        setupRecyclerView()

        // Load data barang dari database
        loadBarangData()
    }

    private fun setupRecyclerView() {
        barangAdapter = BarangAdapter(emptyList()) { barang ->
            // Item click listener
            Toast.makeText(requireContext(), "Klik: ${barang.nama_barang}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewBarang.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = barangAdapter
        }
    }

    private fun loadBarangData() {
        val barangList: List<Barang> = dbHelper.getAllBarang()
        if (barangList.isNotEmpty()) {
            barangAdapter.updateData(barangList)
        } else {
            Toast.makeText(requireContext(), "Tidak ada barang ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
}
