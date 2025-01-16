package com.example.bjbo.database
/*


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bjbo.model.User

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bjbo_users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USER = "users"

        private const val CREATE_TABLE_USER = """
            CREATE TABLE $TABLE_USER (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_user TEXT NOT NULL,
                username TEXT,
                password TEXT NOT NULL,
                nama_depan TEXT,
                nama_belakang TEXT,
                email TEXT NOT NULL UNIQUE,
                nomor_hp TEXT,
                foto_profil TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Membuat tabel user pertama kali saat database dibuat
        db?.execSQL(CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Menghapus tabel lama jika ada pembaruan versi
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // Fungsi untuk registrasi user baru (email dan password saja)
    fun registerUser(email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_user", email) // ID user bisa sementara disamakan dengan email
            put("email", email)
            put("password", password)
        }
        val result = db.insert(TABLE_USER, null, values)
        db.close()
        return result != -1L
    }

    // Fungsi untuk login menggunakan email atau username
    fun login(identifier: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USER WHERE (email = ? OR username = ?) AND password = ?",
            arrayOf(identifier, identifier, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Fungsi untuk mendapatkan data user berdasarkan email atau username
    fun getUser(identifier: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USER WHERE email = ? OR username = ?",
            arrayOf(identifier, identifier)
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                id_user = cursor.getString(cursor.getColumnIndexOrThrow("id_user")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")) ?: "",
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                nama_depan = cursor.getString(cursor.getColumnIndexOrThrow("nama_depan")) ?: "",
                nama_belakang = cursor.getString(cursor.getColumnIndexOrThrow("nama_belakang")) ?: "",
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                nomor_hp = cursor.getString(cursor.getColumnIndexOrThrow("nomor_hp")) ?: "",
                foto_profil = cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"))
            )
            cursor.close()
            db.close()
            user
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Fungsi untuk memperbarui informasi user
    fun updateUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("nama_depan", user.nama_depan)
            put("nama_belakang", user.nama_belakang)
            put("nomor_hp", user.nomor_hp)
            put("foto_profil", user.foto_profil)
        }
        val result = db.update(TABLE_USER, values, "email = ?", arrayOf(user.email))
        db.close()
        return result > 0
    }

    // Fungsi untuk menampilkan semua user (untuk debugging)
    fun logAllUsers() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username")) ?: "N/A"
            println("DEBUG: User - ID: $id, Email: $email, Username: $username")
        }
        cursor.close()
        db.close()
    }
}
*/