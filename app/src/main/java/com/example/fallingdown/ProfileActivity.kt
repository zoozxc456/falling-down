package com.example.fallingdown

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val username = sp.getString("username","")
        findViewById<TextView>(R.id.txt_profile_username).text =username!!

        val backToPreviousActivityButton = findViewById<ImageView>(R.id.btn_profile_back_page)
        backToPreviousActivityButton.setOnClickListener {
            finish()
        }

        val logoutButton = findViewById<Button>(R.id.btn_profile_logout)
        logoutButton.setOnClickListener {

            sp.edit().putString("acc", "").apply()
            finish()
        }

        findViewById<Button>(R.id.btn_profile_edit_username).setOnClickListener {
            startActivity(Intent(this,EditUsernameActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val username = sp.getString("username","")
        findViewById<TextView>(R.id.txt_profile_username).text =username!!
    }
}