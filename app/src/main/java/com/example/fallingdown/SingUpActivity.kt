package com.example.fallingdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fallingdown.model.SignUpRequestModel
import com.example.fallingdown.model.SignUpResponseModel
import com.example.fallingdown.service.SignUpService
import retrofit2.Call
import retrofit2.Response

class SingUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        findViewById<Button>(R.id.btn_sign_up_create).setOnClickListener {

            val account = findViewById<EditText>(R.id.edit_sign_up_account).text.toString()
            val password = findViewById<EditText>(R.id.edit_sign_up_password).text.toString()
            val username = findViewById<EditText>(R.id.edit_sign_up_username).text.toString()

            val requestModel = SignUpRequestModel(account, password, username)

            SignUpService.retrofitService.signup(requestModel)
                .enqueue(object : retrofit2.Callback<SignUpResponseModel> {
                    override fun onResponse(
                        call: Call<SignUpResponseModel>,
                        response: Response<SignUpResponseModel>
                    ) {
                        Log.d("123",response.body().toString())
                        finish()
                    }

                    override fun onFailure(call: Call<SignUpResponseModel>, t: Throwable) {
                        Log.e("error",t.message.toString())
                    }

                })
        }

        findViewById<Button>(R.id.btn_sign_up_reject).setOnClickListener {
            finish()
        }
    }
}