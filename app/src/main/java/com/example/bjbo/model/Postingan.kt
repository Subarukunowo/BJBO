import java.math.BigInteger

data class ApiResponse<T>(
    val success: Boolean,
    val data: T
)

data class Postingan(
    val username: String,
    val price: BigInteger,
    val category: String,
    val description: String,
    var image: String?,
    val lokasi: String,
    val status: String,
    val user_id: BigInteger,
    val name: String,
    val updated_at: String,
    val created_at: String,
    val id: Int
)
