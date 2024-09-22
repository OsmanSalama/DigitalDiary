package com.example.digitaldiaryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import java.io.File
import java.io.IOException

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "files.db"
        private const val DATABASE_VERSION = 2 // Changed version to 2 for the upgrade
        const val TABLE_NAME = "files"
        const val COLUMN_ID = "id"
        const val COLUMN_FILE_NAME = "file_name"
        const val COLUMN_FILE_PATH = "file_path"
        const val COLUMN_DATE = "date_saved" // New column for the save date
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FILE_NAME TEXT, " +
                "$COLUMN_FILE_PATH TEXT, " +
                "$COLUMN_DATE TEXT)" // Create the new column
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Modified insert method to include date
    fun insertFile(fileName: String, filePath: String, dateSaved: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FILE_NAME, fileName)
            put(COLUMN_FILE_PATH, filePath)
            put(COLUMN_DATE, dateSaved) // Insert the date into the new column
        }
        return db.insert(TABLE_NAME,null, contentValues)
    }

    fun getAllFiles(): List<Triple<String, String, String>> { // I have updated this to return date as well
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val files = mutableListOf<Triple<String, String, String>>() // Using Triple for file name, path, and date

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_NAME))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_PATH))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) // Retrieve the saved date
            files.add(Triple(name, path, date))
        }
        cursor.close()
        return files
    }

    fun getFilesByDate(date: String): List<Pair<String, String>> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null,
            null,
            null
        )
        val files = mutableListOf<Pair<String, String>>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_NAME))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_PATH))
            files.add(Pair(name, path))
        }
        cursor.close()
        return files
    }

    fun fileExists(fileName: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "files",
            arrayOf("id"),
            "file_name = ?",
            arrayOf(fileName),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun updateFile(fileName: String, filePath: String, dateSaved: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("file_path", filePath)
            put("date_saved", dateSaved)
        }
        db.update("files", values, "file_name = ?", arrayOf(fileName))
    }

    fun deleteFile(fileName: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_FILE_NAME = ?", arrayOf(fileName))
    }

    fun deleteAllFiles(context: Context) {
        val db = readableDatabase

        // Query to get all file names
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_FILE_NAME), null, null, null, null, null)

        // Iterate through all file names and delete them
        while (cursor.moveToNext()) {
            val fileName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_NAME))
            val fileToDelete = File(context.filesDir, fileName)

            if (fileToDelete.exists()) {
                val deleted = fileToDelete.delete()
                if (!deleted) {
                    throw IOException("Failed to delete file: $fileName")
                }
            }
        }
        cursor.close()

        // Delete all entries from the database
        db.delete(TABLE_NAME, null, null)
    }


}
