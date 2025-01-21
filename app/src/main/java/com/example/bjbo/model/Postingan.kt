import java.math.BigInteger

data class ApiResponse<T>(
    val success: Boolean,
    val data: T
)

data class Postingan(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val description: String,
    var image: String?,
    val lokasi: String,
    val user_id: Int,
    val username: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val ulasan: List<Ulasan> // List untuk menampung ulasan terkait postingan
)

data class Ulasan(
    val id: Int,
    val postingan_id: Int,
    val user_id: Int,
    val rating: Int,
    val komentar: String,
    val created_at: String,
    val updated_at: String,
    val user: User // Detail pengguna yang memberikan ulasan
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val nomor_hp: String?,
    val email_verified_at: String?,
    val password: String, // Tidak disarankan untuk langsung ditampilkan di aplikasi
    val google_id: String?,
    val profile_picture: String?,
    val kelamin: String?,
    val alamat: String?,
    val is_blocked: Int,
    val remember_token: String?,
    val created_at: String,
    val updated_at: String
)
