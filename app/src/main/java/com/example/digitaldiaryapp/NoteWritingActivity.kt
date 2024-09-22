package com.example.digitaldiaryapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime


class NoteWritingActivity : AppCompatActivity() {
    // Declaring objects
    private lateinit var backButton: ImageView
    private lateinit var textViewSave: TextView
    private lateinit var editTextNoteTitle: EditText
    private lateinit var editTextDiaryEntry: EditText
    private var currentFileName: String? = null
    private lateinit var textDiaryTitle:String
    private lateinit var floatingActionButtonSaveButton: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_writing)

        // Window settings
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.buttonColoursGruvboxLight)

        // View bindings
        backButton = findViewById(R.id.imageViewBack)
        textViewSave = findViewById(R.id.textViewDeleteNote)
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle)
        editTextDiaryEntry = findViewById(R.id.diaryEntryEditText)
        floatingActionButtonSaveButton = findViewById(R.id.floatingActionButtonSaveNote)
        textDiaryTitle = intent.getStringExtra("note_title").toString()

        // setting the default editText title
        val defaultTitle =
            "${LocalDate.now()} ${LocalTime.now().hour}:${LocalTime.now().minute}:${LocalTime.now().second}"
        editTextNoteTitle.setText(defaultTitle)

        // making onClickListener for the back button
        backButton.setOnClickListener {
            // finishing the note writing activity, and exiting to the home screen
            finish()
        }

        // Retrieve the note title and content passed from the adapter
        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")
        currentFileName = intent.getStringExtra("note_file_name")

        // Set the note title and content in the EditTexts
        editTextNoteTitle.setText(noteTitle)
        editTextDiaryEntry.setText(noteContent)

        // Save the note when the save button is clicked
        textViewSave.setOnClickListener {
            deleteCurrentNote()
        }

        floatingActionButtonSaveButton.setOnClickListener {
            val updatedFileName: String = if (editTextNoteTitle.text.toString().isBlank()) {
                "${LocalDate.now()} ${LocalTime.now().hour}:${LocalTime.now().minute}:${LocalTime.now().second}"
            } else {
                "${editTextNoteTitle.text}"
            }

            val updatedText: String = editTextDiaryEntry.text.toString()

            // Get the current date
            val currentDate = LocalDate.now().toString() // New: Save the date

            saveTextToFile(updatedFileName, updatedText, currentDate)
        }
    }

    private fun saveTextToFile(fileName: String, text: String, dateSaved: String) {
        try {
            // If the note's title was changed, delete the old file
            if (currentFileName != null && currentFileName != fileName) {
                val oldFile = File(filesDir, currentFileName!!)
                if (oldFile.exists()) {
                    oldFile.delete()
                }
            }

            // Write the updated content to the new file
            val fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
            fileOutputStream.use { outputStream ->
                outputStream.write(text.toByteArray())
            }

            // Update the file name and date in the database
            val dbHelper = DatabaseHelper(this)
            val filePath = filesDir.absolutePath + "/" + fileName
            if (dbHelper.fileExists(fileName)) {
                dbHelper.updateFile(fileName, filePath, dateSaved)
            } else {
                dbHelper.insertFile(fileName, filePath, dateSaved)
            }

            // Restartin the MainActivity to update the note list
            val restartIntent = Intent(this, MainActivity::class.java)
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(restartIntent)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCurrentNote() {
        val fileName = textDiaryTitle

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you would like to delete this note?")

        builder.setPositiveButton("YES") { _, _ ->
            try {
                // Delete the file from internal storage
                Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show()
                val fileToDelete = File(filesDir, fileName)
                if (fileToDelete.exists()) {
                    val deleted = fileToDelete.delete()
                    if (!deleted) {
                        throw IOException("Failed to delete file: $fileName")
                    }
                }

                // Remove the entry from the database
                val dbHelper = DatabaseHelper(this)
                if (dbHelper.fileExists(fileName)) {
                    Toast.makeText(this, "this file exists", Toast.LENGTH_SHORT).show()
                    dbHelper.deleteFile(fileName)
                }

                // Restart the MainActivity to update the note list
                val restartIntent = Intent(this, MainActivity::class.java)
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(restartIntent)

                Toast.makeText(this, "Note ($fileName) deleted successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}
