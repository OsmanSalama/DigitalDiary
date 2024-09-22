package com.example.digitaldiaryapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


@Suppress("DEPRECATION")
class SettingsViewActivity : AppCompatActivity() {
    // declaring attributes
    private lateinit var imageViewBack:ImageView
    private lateinit var imageViewCalendar: ImageView
    private lateinit var mainActivityDisplay: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var settingsAdapter: SettingsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_view)

        // assigning attributes to the actual views
        imageViewBack = findViewById(R.id.imageViewBack)
        imageViewCalendar = findViewById(R.id.imageViewCalendar)
        mainActivityDisplay = findViewById(R.id.imageViewNoteIcon)
        recyclerView = findViewById(R.id.settingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // on click listener for the back image view
        imageViewBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // on click listener for calender image
        imageViewCalendar.setOnClickListener {
            val intent = Intent(this, CalenderViewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // onClickListener for displaying the Home activity
        mainActivityDisplay.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Get the window associated with the current activity
        val window = window
        // Allow the status bar to draw its own background
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.buttonColoursGruvboxLight)
        
        val settingItems = listOf(
            SettingItem("Theme", R.drawable.bin),
            SettingItem("Privacy Policy", R.drawable.calendar_light),
            SettingItem("About", R.drawable.note_light),
            SettingItem("Delete All Notes", R.drawable.bin)
        )
        val dbHelper = DatabaseHelper(this)

        settingsAdapter = SettingsAdapter(settingItems) { settingItem ->
            when (settingItem.title) {
                "Theme" -> {
                    // TODO: Open theme settings
                    Toast.makeText(this, "Under Development, the Base theme is already good"
                        , Toast.LENGTH_SHORT).show()
                }
                "Privacy Policy" -> {
                    val intent = Intent(this, PrivacyPolicyActivity::class.java)
                    startActivity(intent)
                }
                "About" ->{
                    val intent = Intent(this, AboutAppActivity::class.java)
                    startActivity(intent)
                }
                "Delete All Notes" -> {
                    val builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this)

                    builder.setTitle("Confirm")
                    builder.setMessage(
                        "Are you sure you would like to delete all the saved notes" +
                                "there is no undo?"
                    )

                    builder.setPositiveButton(
                        "YES",
                        DialogInterface.OnClickListener { _, _ ->
                            dbHelper.deleteAllFiles(this)
                        })

                    builder.setNegativeButton(
                        "NO",
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.dismiss()
                        })

                    builder.create()?.show()
                }
            }
        }

        recyclerView.adapter = settingsAdapter
    }
}