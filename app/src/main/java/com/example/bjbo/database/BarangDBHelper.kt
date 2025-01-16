package com.example.bjbo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bjbo.model.Barang

class BarangDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "barang.db"
        private const val DATABASE_VERSION = 1

        // Nama tabel dan kolom
        private const val TABLE_BARANG = "barang"
        private const val COLUMN_ID_BARANG = "id_barang"
        private const val COLUMN_NAMA_BARANG = "nama_barang"
        private const val COLUMN_KATEGORI = "kategori"
        private const val COLUMN_HARGA = "harga"
        private const val COLUMN_DESKRIPSI_BARANG = "deskripsi_barang"
        private const val COLUMN_STOCK = "stock"
        private const val COLUMN_GAMBAR = "gambar"
        private const val COLUMN_LOKASI = "lokasi"

        private const val CREATE_TABLE_BARANG = """
            CREATE TABLE $TABLE_BARANG (
                $COLUMN_ID_BARANG TEXT PRIMARY KEY,
                $COLUMN_NAMA_BARANG TEXT NOT NULL,
                $COLUMN_KATEGORI TEXT NOT NULL,
                $COLUMN_HARGA INTEGER NOT NULL,
                $COLUMN_DESKRIPSI_BARANG TEXT NOT NULL,
                $COLUMN_STOCK INTEGER NOT NULL,
                $COLUMN_GAMBAR TEXT NOT NULL,
                $COLUMN_LOKASI TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_BARANG)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BARANG")
        onCreate(db)
    }

    // Fungsi untuk menambahkan barang
    fun insertBarang(barang: Barang): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_BARANG, barang.id_barang)
            put(COLUMN_NAMA_BARANG, barang.nama_barang)
            put(COLUMN_KATEGORI, barang.kategori)
            put(COLUMN_HARGA, barang.harga)
            put(COLUMN_DESKRIPSI_BARANG, barang.deskripsi_barang)
            put(COLUMN_STOCK, barang.stock)
            put(COLUMN_GAMBAR, barang.gambar)
            put(COLUMN_LOKASI, barang.lokasi)
        }
        val result = db.insert(TABLE_BARANG, null, values)
        db.close()
        return result != -1L
    }

    // Fungsi untuk mengambil semua barang
    fun getAllBarang(): List<Barang> {
        val barangList = mutableListOf<Barang>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_BARANG", null)
        if (cursor.moveToFirst()) {
            do {
                val barang = Barang(
                    id_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_BARANG)),
                    nama_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG)),
                    kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI)),
                    harga = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HARGA)),
                    deskripsi_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI_BARANG)),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)),
                    gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR)),
                    lokasi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOKASI))
                )
                barangList.add(barang)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return barangList
    }

    // Fungsi untuk mengambil barang berdasarkan kategori
    fun getBarangByKategori(kategori: String): List<Barang> {
        val barangList = mutableListOf<Barang>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_BARANG WHERE $COLUMN_KATEGORI = ?", arrayOf(kategori))
        if (cursor.moveToFirst()) {
            do {
                val barang = Barang(
                    id_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_BARANG)),
                    nama_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG)),
                    kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI)),
                    harga = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HARGA)),
                    deskripsi_barang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI_BARANG)),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)),
                    gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR)),
                    lokasi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOKASI))
                )
                barangList.add(barang)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return barangList
    }

    // Fungsi untuk memperbarui barang
    fun updateBarang(barang: Barang): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA_BARANG, barang.nama_barang)
            put(COLUMN_KATEGORI, barang.kategori)
            put(COLUMN_HARGA, barang.harga)
            put(COLUMN_DESKRIPSI_BARANG, barang.deskripsi_barang)
            put(COLUMN_STOCK, barang.stock)
            put(COLUMN_GAMBAR, barang.gambar)
            put(COLUMN_LOKASI, barang.lokasi)
        }
        val result = db.update(TABLE_BARANG, values, "$COLUMN_ID_BARANG = ?", arrayOf(barang.id_barang))
        db.close()
        return result > 0
    }

    // Fungsi untuk menghapus barang
    fun deleteBarang(id_barang: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_BARANG, "$COLUMN_ID_BARANG = ?", arrayOf(id_barang))
        db.close()
        return result > 0
    }
}
