import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bjbo.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_LOGIN = "login"
        private const val CREATE_TABLE_LOGIN = """
            CREATE TABLE $TABLE_LOGIN (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email STRING,
                password STRING
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_LOGIN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN")
        onCreate(db)
    }

    // Menambahkan user baru ke database
    fun addUser(email: String, password: String) {
        val db = writableDatabase
        val query = "INSERT INTO $TABLE_LOGIN (email, password) VALUES (?, ?)"
        db.execSQL(query, arrayOf(email, password))
    }

    // Mengecek login
    fun checkLogin(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_LOGIN WHERE email = ? AND password = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
