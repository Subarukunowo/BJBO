import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bjbo.R
import android.content.Intent
import java.text.NumberFormat
import java.util.Locale

import com.example.bjbo.DetailPostinganActivity

class PostinganAdapter(
    private val context: Context,
    private val postinganList: List<Postingan>
) : RecyclerView.Adapter<PostinganAdapter.PostinganViewHolder>() {

    inner class PostinganViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivPostinganImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvPostinganName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPostinganPrice)
        val locationTextView: TextView = itemView.findViewById(R.id.tvPostinganLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostinganViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_postingan, parent, false)
        return PostinganViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostinganViewHolder, position: Int) {
        val postingan = postinganList[position]

        // Set data ke TextView
        holder.nameTextView.text = postingan.name ?: "Tidak ada nama"

        // Format harga menggunakan NumberFormat
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(postingan.price)
        holder.priceTextView.text = formattedPrice

        holder.locationTextView.text = postingan.lokasi ?: "Lokasi tidak tersedia"

        // Memuat gambar menggunakan Glide
        Glide.with(context)
            .load(postingan.image)
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.baseline_broken_image_24)
            .into(holder.imageView)

        // Navigasi ke DetailPostinganActivity saat item diklik
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailPostinganActivity::class.java).apply {
                putExtra("name", postingan.name)
                putExtra("price", postingan.price)
                putExtra("location", postingan.lokasi)
                putExtra("description", postingan.description ?: "Tidak ada deskripsi")
                putExtra("image", postingan.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postinganList.size
    }
}
