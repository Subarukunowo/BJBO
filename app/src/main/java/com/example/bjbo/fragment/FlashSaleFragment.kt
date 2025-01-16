package com.example.bjbo.fragment
/*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.DetailProductActivity
import com.example.bjbo.databinding.FragmentFlashsaleBinding
import com.example.bjbo.model.FlashSale
import com.example.bjbo.network.ApiClient
import com.example.bjbo.ui.FlashSaleAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlashSaleFragment : Fragment() {

    private var _binding: FragmentFlashsaleBinding? = null
    private val binding get() = _binding!!
    private lateinit var flashSaleAdapter: FlashSaleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashsaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchFlashSales()
    }

    private fun setupRecyclerView() {
        flashSaleAdapter = FlashSaleAdapter(emptyList()) { flashSale ->
            // Klik item untuk membuka DetailProductActivity
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("ID_FLASHSALE", flashSale.id_flashsale) // Mengirimkan id_flashsale
            startActivity(intent)
        }

        binding.recyclerViewFlashSale.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = flashSaleAdapter
        }
    }
    private fun fetchFlashSales() {
        lifecycleScope.launch {
            try {
                // Panggil suspend function
                val flashSales = ApiClient.instance.getFlashSales()
                // Perbarui adapter
                flashSaleAdapter.updateData(flashSales)
            } catch (e: Exception) {
                // Tampilkan pesan error
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/