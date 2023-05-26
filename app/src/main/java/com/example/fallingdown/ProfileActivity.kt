package com.example.fallingdown

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backToPreviousActivityButton = findViewById<ImageView>(R.id.btn_profile_back_page)
        backToPreviousActivityButton.setOnClickListener {
            finish()
        }

        val logoutButton = findViewById<Button>(R.id.btn_profile_logout)
        logoutButton.setOnClickListener {
            val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            sp.edit().putString("acc", "").apply()
            finish()
        }
    }
}