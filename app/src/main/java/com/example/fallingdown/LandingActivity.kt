package com.example.fallingdown

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ActionMenuView.LayoutParams
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginView = findViewById<View>(R.id.login_bottom_sheet)
        updateLoginViewHeight(loginView)

        var closeLoginViewBtn = findViewById<Button>(R.id.btn_close_loginView)
        var bottomBehavior = BottomSheetBehavior.from(loginView)

        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    closeLoginViewBtn.visibility = View.INVISIBLE
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    closeLoginViewBtn.visibility = View.VISIBLE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        closeLoginViewBtn.setOnClickListener {
            bottomBehavior.isHideable=true
            bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
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
