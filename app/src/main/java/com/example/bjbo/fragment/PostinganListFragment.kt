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
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostinganListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postinganAdapter: PostinganAdapter
    private val postinganList = mutableListOf<Postingan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_postingan_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        // LinearLayoutManager horizontal
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        postinganAdapter = PostinganAdapter(requireContext(), postinganList)
        recyclerView.adapter = postinganAdapter

        loadPostinganData()

        return view
    }

    private fun loadPostinganData() {
        Log.d(
            "API Request",
            "Fetching data from: ${ApiClient.instance.getAllPostingan().request().url}"
        )

        ApiClient.instance.getAllPostingan()
            .enqueue(object : Callback<ApiResponse<Postingan>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ApiResponse<Postingan>>,
                    response: Response<ApiResponse<Postingan>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && apiResponse.success) {
                            postinganList.clear()

                            // Filter hanya postingan dengan status 'disetujui'
                            val filteredData = apiResponse.data.filter { it.status == "disetujui" }

                            // Batasi hanya 5 elemen pertama setelah filter
                            val limitedData = filteredData.take(5)

                            // Update image URL untuk setiap postingan
                            limitedData.forEachIndexed { index, postingan ->
                                postingan.image =
                                    ApiClient.getFullimageUrl(postingan.image)
                                Log.d("Image URL", "Position $index: ${postingan.image}")
                            }

                            postinganList.addAll(limitedData)
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
                        Log.e("API Error", "Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Postingan>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("API Error", "Failure: ${t.message}")
                }
            })
    }
}
