package com.example.bjbo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.databinding.FragmentUlasanListBinding
import com.example.bjbo.model.ApiResponse
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

        // Ambil postinganId dari arguments
        val postinganId = arguments?.getInt(ARG_POSTINGAN_ID) ?: -1
        Log.d("UlasanListFragment", "postinganId yang digunakan: $postinganId")

        if (postinganId != -1) {
            loadUlasanFromApi(postinganId)
        } else {
            showEmptyState("ID postingan tidak valid.")
        }
    }

    private fun setupRecyclerView() {
        ulasanAdapter = UlasanAdapter(ulasanList)
        binding.recyclerViewUlasan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ulasanAdapter
        }
    }

    private fun loadUlasanFromApi(postinganId: Int) {
        ApiClient.instance.getUlasansByPostinganId(postinganId).enqueue(object : Callback<ApiResponse<List<Ulasan>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Ulasan>>>,
                response: Response<ApiResponse<List<Ulasan>>>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    Log.d("UlasanListFragment", "Data ulasan diterima: $data")
                    if (!data.isNullOrEmpty()) {
                        showUlasans(data)
                    } else {
                        showEmptyState("Belum ada ulasan untuk postingan ini.")
                    }
                } else {
                    showEmptyState("Gagal memuat ulasan. Kesalahan server.")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Ulasan>>>, t: Throwable) {
                showEmptyState("Kesalahan jaringan: ${t.localizedMessage}")
                Toast.makeText(requireContext(), "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showUlasans(ulasans: List<Ulasan>) {
        ulasanList.clear()
        ulasanList.addAll(ulasans)
        ulasanAdapter.notifyDataSetChanged()
        binding.tvErrorMessage.visibility = View.GONE
        binding.recyclerViewUlasan.visibility = View.VISIBLE
    }

    private fun showEmptyState(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.recyclerViewUlasan.visibility = View.GONE
        ulasanList.clear()
        ulasanAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_POSTINGAN_ID = "arg_postingan_id"

        fun newInstance(postinganId: Int): UlasanListFragment {
            val fragment = UlasanListFragment()
            val args = Bundle().apply {
                putInt(ARG_POSTINGAN_ID, postinganId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
