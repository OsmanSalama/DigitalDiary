package com.example.digitaldiaryapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrivacyPolicyActivity : AppCompatActivity() {
    // declaring objects
    private lateinit var imageViewBack:ImageView
    private lateinit var textViewPrivacyPolicy: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy_policy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialising objects
        imageViewBack = findViewById(R.id.imageViewBackButton)
        textViewPrivacyPolicy = findViewById(R.id.textViewPrivacyPolicy)

        // onClickListener for the image back button
        imageViewBack.setOnClickListener {
            finish();
        }

        // Get the window associated with the current activity
        val window = window
        // Allow the status bar to draw its own background
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.buttonColoursGruvboxLight)

        val scrollView = ScrollView(this)
        val textView = TextView(this)

        // setting the text of the text view
        textViewPrivacyPolicy.text = Html.fromHtml(getPrivacyPolicyText(), Html.FROM_HTML_MODE_COMPACT)
    }

    private fun getPrivacyPolicyText(): String {
        return """
        <b><font size="1621312">Privacy Policy for GruvNote</font></b><br><br>

        Last updated: 11/09/2024<br><br>

        <b>1. Introduction</b><br>
        Welcome to GruvNote. We are committed to protecting your privacy and ensuring the 
        security of your personal information. This Privacy Policy explains how our app 
        handles your data.<br><br>

        <b>2. Data Collection and Storage</b><br>
        Our app is designed with your privacy in mind:
        - All data you enter into the app (including diary entries and personal information) 
          is stored locally on your device.
        - We do not collect, transmit, or store any of your data on external servers.
        - No information leaves your device through our app.<br><br>

        <b>3. Data Security</b><br>
        The security of your data is important to us:
        - Your data is stored in a local database within the app's secure storage area 
          on your device.
        - We implement standard security measures provided by your device's operating 
          system to protect this local storage.
        - However, the overall security of your data also depends on your device's 
          security settings and measures.<br><br>

        <b>4. Data Retention and Deletion</b><br>
        - Your data remains on your device until you choose to delete it or uninstall the app.
        - Currently, there is no backup feature. If you uninstall the app, all data will 
          be permanently deleted.
        - We recommend regularly backing up your device to prevent data loss.<br><br>

        <b>5. Third-Party Access</b><br>
        - We do not share your data with any third parties, because we do not have access to it.
        - No advertising networks, analytics providers, or other external services are used 
          within the app.<br><br>

        <b>6. Children's Privacy</b><br>
        - Our app does not knowingly collect or store personal information from children 
          under 13.
        - As all data is stored locally, parents or guardians are responsible for monitoring 
          their children's use of the app.<br><br>

        <b>7. Changes to This Privacy Policy</b><br>
        - We may update our Privacy Policy from time to time. We will notify you of any changes 
          by posting the new Privacy Policy on this page and updating the "Last updated" date.<br><br>

        <b>8. Contact Us</b><br>
        - If you have any questions about this Privacy Policy, please contact us at:
          namso.me@gmail.com<br><br><br>
          
        <b>YES, I DID NOT WRITE ALL OF THIS</b>
    """.trimIndent()
    }




}