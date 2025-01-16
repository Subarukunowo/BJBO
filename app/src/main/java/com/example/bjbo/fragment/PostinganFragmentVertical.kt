package com.example.bjbo.fragment

import ApiResponse
import Postingan
import PostinganAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostinganFragmentVertical : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postinganAdapter: PostinganAdapter
    private val postinganList = mutableListOf<Postingan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_postingan_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set RecyclerView LayoutManager to vertical
        recyclerView.layoutManager = LinearLayoutManager(context)

        postinganAdapter = PostinganAdapter(requireContext(), postinganList)
        recyclerView.adapter = postinganAdapter

        loadPostinganData()

        return view
    }

    private fun loadPostinganData() {
        ApiClient.instance.getAllPostingan().enqueue(object : Callback<ApiResponse<Postingan>> {
            override fun onResponse(
                call: Call<ApiResponse<Postingan>>,
                response: Response<ApiResponse<Postingan>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        postinganList.clear()
                        val approvedPostingan = apiResponse.data.filter { it.status == "disetujui" }
                        postinganList.addAll(approvedPostingan)
                        postinganAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Data tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Postingan>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
