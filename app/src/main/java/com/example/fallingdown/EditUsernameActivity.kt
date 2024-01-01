package com.example.fallingdown

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.fallingdown.model.SignUpResponseModel
import com.example.fallingdown.service.EditUsernameResponseModel
import com.example.fallingdown.service.EditUsernameService
import retrofit2.Call
import retrofit2.Response

class EditUsernameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_username)

        val usernameEditor = findViewById<EditText>(R.id.edit_username)

        findViewById<Button>(R.id.btn_edit_username_accept).setOnClickListener {
            // API
            val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            val userId = sp.getString("acc", "")!!
            val username = usernameEditor.text.toString()
            EditUsernameService.retrofitService.edit(userId = userId, newUsername = username)
                .enqueue(object : retrofit2.Callback<EditUsernameResponseModel> {
                    override fun onResponse(
                        call: Call<EditUsernameResponseModel>,
                        response: Response<EditUsernameResponseModel>
                    ) {
//                        Log.d("a", response.raw().toString())
                        sp.edit().putString("username", username).apply()
                        Log.d("a", sp.getString("username","")!!)
                        finish()
                    }

                    override fun onFailure(call: Call<EditUsernameResponseModel>, t: Throwable) {
                        Log.d("a", t.message.toString())
                        finish()
                    }
                })
        }

        findViewById<Button>(R.id.btn_edit_username_reject).setOnClickListener {
            finish()
        }
    }
}