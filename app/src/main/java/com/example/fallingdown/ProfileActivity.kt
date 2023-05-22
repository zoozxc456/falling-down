package com.example.fallingdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backToPreviousActivityButton = findViewById<ImageView>(R.id.btn_profile_back_page)
        backToPreviousActivityButton.setOnClickListener {
            finish()
        }
    }
}