package com.example.app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bjbo.db"
        private const val DATABASE_VERSION = 1

        // Nama tabel login
        private const val TABLE_LOGIN = "login"

        // Query untuk membuat tabel login
        private const val CREATE_TABLE_LOGIN = """
            CREATE TABLE $TABLE_LOGIN (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Membuat tabel login saat database pertama kali dibuat
        db?.execSQL(CREATE_TABLE_LOGIN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Hapus tabel lama jika ada versi baru
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN")
        onCreate(db)
    }

    // Menambahkan user baru ke database
    fun addUser(email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("email", email)
        values.put("password", password)
        val result = db.insert(TABLE_LOGIN, null, values)
        db.close()
        return result != -1L
    }

    // Mengecek apakah user dengan email dan password tertentu ada di database
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
}
