package com.example.fallingdown

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ActionMenuView.LayoutParams
import androidx.appcompat.app.AppCompatActivity

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginView = findViewById<View>(R.id.login_bottom_sheet)
        updateLoginViewHeight(loginView)
    }

    private fun updateLoginViewHeight(loginView: View) {
        loginView.post {
            val height = loginView.height
            val params = loginView.layoutParams
            val offsetOfParent = 50

            params.height = (height - offsetOfParent)
            params.width = LayoutParams.MATCH_PARENT
            loginView.layoutParams = params
        }
    }
}
