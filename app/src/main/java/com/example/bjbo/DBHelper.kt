package com.example.bjbo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bjbo.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_LOGIN = "login"

        private const val CREATE_TABLE_LOGIN = """
            CREATE TABLE $TABLE_LOGIN (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Membuat tabel login pertama kali saat database dibuat
        db?.execSQL(CREATE_TABLE_LOGIN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Menghapus tabel lama jika ada pembaruan versi
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN")
        onCreate(db)
    }

    // Menambahkan user baru ke database
    fun addUser(email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("password", password)
        }
        val result = db.insert(TABLE_LOGIN, null, values)
        db.close()
        return result != -1L
    }

    // Mengecek apakah user dengan email dan password ada di database
    fun checkLogin(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_LOGIN WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Menampilkan semua pengguna (untuk debug)
    fun logAllUsers() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_LOGIN", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            println("DEBUG: User - ID: $id, Email: $email, Password: $password")
        }
        cursor.close()
        db.close()
    }

    // Menghapus semua data di tabel login
    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_LOGIN")
        db.close()
    }
}
