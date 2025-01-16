package com.example.bjbo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.databinding.FragmentUlasanListBinding
import com.example.bjbo.model.Ulasan
import com.example.bjbo.network.ApiClient
import com.example.bjbo.ui.UlasanAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UlasanListFragment : Fragment() {

    private var _binding: FragmentUlasanListBinding? = null
    private val binding get() = _binding!!
    private val ulasanList = mutableListOf<Ulasan>()
    private lateinit var ulasanAdapter: UlasanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUlasanListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        setupRecyclerView()

        // Load ulasan from API
        loadUlasan()
    }

    private fun setupRecyclerView() {
        ulasanAdapter = UlasanAdapter(ulasanList)
        binding.recyclerViewUlasan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ulasanAdapter
        }
    }

    private fun loadUlasan() {
        // Make API call
        ApiClient.instance.getAllUlasans().enqueue(object : Callback<List<Ulasan>> {
            override fun onResponse(call: Call<List<Ulasan>>, response: Response<List<Ulasan>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    // Populate RecyclerView with API data
                    ulasanList.clear()
                    ulasanList.addAll(response.body()!!)
                    ulasanAdapter.notifyDataSetChanged()
                    binding.tvErrorMessage.visibility = View.GONE
                } else {
                    // Handle empty or unsuccessful response
                    ulasanList.clear()
                    ulasanAdapter.notifyDataSetChanged()
                    binding.tvErrorMessage.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = "Belum ada ulasan."
                }
            }

            override fun onFailure(call: Call<List<Ulasan>>, t: Throwable) {
                // Handle network error
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.tvErrorMessage.text = "Gagal memuat ulasan: ${t.localizedMessage}"
                ulasanList.clear()
                ulasanAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
