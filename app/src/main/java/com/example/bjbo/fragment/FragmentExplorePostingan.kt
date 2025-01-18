package com.example.bjbo.fragment
import Postingan
import PostinganAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R

import com.example.bjbo.model.ApiResponse

import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePostinganFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postinganAdapter: PostinganAdapter
    private val postinganList = mutableListOf<Postingan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        setupRecyclerView(view)
        loadAllPostinganData()
        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewExplore)
        recyclerView.layoutManager = LinearLayoutManager(context) // Menggunakan LinearLayoutManager
        postinganAdapter = PostinganAdapter(requireContext(), postinganList)
        recyclerView.adapter = postinganAdapter
    }

    private fun loadAllPostinganData() {
        Log.d("API Request", "Fetching data from: ${ApiClient.instance.getAllPostingan().request().url}")

        ApiClient.instance.getAllPostingan()
            .enqueue(object : Callback<ApiResponse<List<Postingan>>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ApiResponse<List<Postingan>>>,
                    response: Response<ApiResponse<List<Postingan>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            if (apiResponse.success) {
                                updatePostinganList(apiResponse.data.orEmpty())
                            } else {
                                showToast("Data tidak ditemukan")
                            }
                        } ?: run {
                            Log.e("API Error", "Respons kosong dari server")
                            showToast("Respons kosong")
                        }
                    } else {
                        handleError(response.code(), response.message())
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<Postingan>>>, t: Throwable) {
                    Log.e("API Error", "Failure: ${t.message}")
                    showToast("Gagal memuat data: ${t.message}")
                }
            })
    }

    private fun updatePostinganList(data: List<Postingan>) {
        postinganList.clear()
        data.forEachIndexed { index, postingan ->
            postingan.image = ApiClient.getFullimageUrl(postingan.image)
            Log.d("Image URL", "Position $index: ${postingan.image}")
        }
        postinganList.addAll(data)
        postinganAdapter.notifyDataSetChanged()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleError(code: Int, message: String) {
        Log.e("API Error", "Error Code: $code, Message: $message")
        showToast("Error: $code - $message")
    }
}
