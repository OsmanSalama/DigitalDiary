package com.example.digitaldiaryapp

import NotesAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity() {
    // declaring components
    private lateinit var floatingActionButtonAddNote: FloatingActionButton
    private lateinit var imageViewCalendar: ImageView
    private lateinit var settingsView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        /// this is supposed to display the splash screen after 100 milliseconds
        Thread.sleep(100)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // assigning components
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote)
        imageViewCalendar = findViewById(R.id.imageViewCalendar)
        settingsView = findViewById(R.id.imageViewSetting)

        // onClick listener to view the note writing activity
        floatingActionButtonAddNote.setOnClickListener {
            val intent = Intent(this, NoteWritingActivity::class.java)
            startActivity(intent)
        }

        // on click listener for calender image
        imageViewCalendar.setOnClickListener {
            val intent = Intent(this, CalenderViewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // onClickListener for displaying the settings activity
        settingsView.setOnClickListener {
            val intent = Intent(this, SettingsViewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Get the window associated with the current activity
        val window = window
        // Allow the status bar to draw its own background
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the very top bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.buttonColoursGruvboxLight)

        // retreiving data to the recycler view
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val databaseHelper = DatabaseHelper(this)
        val notes = databaseHelper.getAllFiles()

        recyclerView.adapter = NotesAdapter(notes) { note ->
            openNoteDetail(note)
        }
    }
    private fun openNoteDetail(note: Triple<String, String, String>) {
        val intent = Intent(this, NoteWritingActivity::class.java)
        intent.putExtra("note_title", note.first)
        intent.putExtra("note_content", File(note.second).readText())
        intent.putExtra("note_file_name", note.second)
        startActivity(intent)
    }
}