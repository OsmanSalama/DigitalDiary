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

class AboutAppActivity : AppCompatActivity() {
    // declaring objects
    private lateinit var imageViewBack: ImageView
    private lateinit var textViewPrivacyPolicy: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about_app)

        // Initialising objects
        imageViewBack = findViewById(R.id.imageViewBackView)
        textViewPrivacyPolicy = findViewById(R.id.textViewAboutApp)

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

        // setting the text of the text view
        textViewPrivacyPolicy.text = Html.fromHtml(getAboutApp(), Html.FROM_HTML_MODE_COMPACT)
    }

    private fun getAboutApp(): String {
        return """
        <b><font size="5">About GruvNote</font></b><br><br>
        
        Welcome to GruvNote, a simple and elegant diary app designed to help you keep track of your thoughts and notes in a user-friendly and colorful way.<br><br>
        
        <b><font size="4">About the Developer</font></b><br>
        Osman Adam, a student studying software development. <br>Over the summer, I dedicated my time to learning Kotlin and Android development, following the roadmap.sh guide for Android developers. I have watched numerous tutorials and applied this knowledge to create practical applications.<br><br>
        
        <b><font size="4">Project Background</font></b><br>
        GruvNote is part of a collection of simple apps I am developing to showcase my skills for my college application. You can explore more about these projects on my personal website: <a href="https://namso.me">namso.me</a>. There, you will find detailed steps, challenges encountered, and solutions implemented for each project.<br><br>
        
        <b><font size="4">Development Journey</font></b><br>
        <b>Tools and Technologies:</b> The app was developed using Kotlin and Android Studio. I focused on core Android development concepts and implemented the project without relying on external libraries. This hands-on approach helped me understand the fundamentals of app development.<br>
        <b>Learning Resources:</b> My learning journey included following tutorials and the roadmap.sh guide for Android development, which provided a structured path for acquiring new skills.<br><br>
        
        <b><font size="4">Future Plans</font></b><br>
        I am excited to continue developing more complex apps, including:<br>
        - <b>ToDo List App:</b> A comprehensive task management tool.<br>
        - <b>Book Reader Library:</b> An app to read files and back up data.<br>
        - <b>And more:</b> Additional projects that will further enhance my skills and provide useful tools.<br><br>
        
        <b><font size="4">Contact Information</font></b><br>
        For any questions, feedback, or inquiries, feel free to reach out via email at: <a href="mailto:namso.me@gmail.com">namso.me@gmail.com</a>.<br><br>
        
        Thank you for exploring GruvNote. I hope you find it useful and enjoy using it as much as I enjoyed creating it!<br>
        <br> <b>AGAIN I DID NOT TYPE ALL OF THIS, NO TIME, BUT IT IS ALL TRUE</b>

    """.trimIndent()
    }
}