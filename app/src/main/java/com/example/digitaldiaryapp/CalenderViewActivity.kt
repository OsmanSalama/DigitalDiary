package com.example.digitaldiaryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class CalenderViewActivity : AppCompatActivity() {

    private lateinit var calendar: CalendarView
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var backButton: ImageView
    private lateinit var imageViewNote: ImageView
    private lateinit var settingsView: ImageView
    private lateinit var textViewNoNotes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender_view)

        calendar = findViewById(R.id.calendarMain)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
        recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        backButton = findViewById(R.id.imageViewBack)
        imageViewNote = findViewById(R.id.imageViewNoteIcon)
        settingsView = findViewById(R.id.imageViewSettingPage)
        textViewNoNotes = findViewById(R.id.textViewMessage)

        dbHelper = DatabaseHelper(this)

        // Load all notes initially
        loadNotes()

        // Set the OnDateChangeListener for the CalendarView
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
            loadNotesByDate(date)
        }

        // on click listener for settings image to display the Settings Activity
        settingsView.setOnClickListener {
            val intent = Intent(this, SettingsViewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Get the window associated with the current activity
        val window = window
        // Allow the status bar to draw its own background
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.buttonColoursGruvboxLight)

        // on click listener to the Home page (MainActivity)
        imageViewNote.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // onClickListener for the back Image view
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    // Function to load all notes
    private fun loadNotes() {
        val allNotes = dbHelper.getAllFiles()
        notesAdapter = NotesAdapter(allNotes) { note ->
            openNoteDetail(note)
        }
        recyclerViewNotes.adapter = notesAdapter
    }

    // Function to load notes by the selected date
    private fun loadNotesByDate(date: String) {
        val notesByDate = dbHelper.getFilesByDate(date)
        if (notesByDate.isNotEmpty()) {
            recyclerViewNotes.visibility = View.VISIBLE
            textViewNoNotes.visibility = View.GONE
            notesAdapter = NotesAdapter(notesByDate.map { Triple(it.first, it.second, date) }) { note ->
                openNoteDetail(note)

            }
        } else {
            recyclerViewNotes.visibility = View.GONE
            textViewNoNotes.visibility = View.VISIBLE
        }
        recyclerViewNotes.adapter = notesAdapter
    }

    // Open the detail of the clicked note
    private fun openNoteDetail(note: Triple<String, String, String>) {
        val intent = Intent(this, NoteWritingActivity::class.java)
        intent.putExtra("note_title", note.first)
        intent.putExtra("note_content", File(note.second).readText())
        intent.putExtra("note_file_name", note.second)
        startActivity(intent)
    }
}
