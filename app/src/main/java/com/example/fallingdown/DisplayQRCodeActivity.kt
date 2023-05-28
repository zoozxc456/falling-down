package com.example.fallingdown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class DisplayQRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_qrcode)

        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sp.getString("userId", "")

        val qrcodeImageView = findViewById<ImageView>(R.id.iv_qrcode)
        val qrCode = generateQrcode(userId!!)
        qrcodeImageView.setImageBitmap(qrCode)

        val backToFriendsActivityButton = findViewById<ImageView>(R.id.btn_display_qrcode_back_page)
        backToFriendsActivityButton.setOnClickListener {
            finish()
        }
    }

    private fun generateQrcode(contents:String):Bitmap{
        val encoder = BarcodeEncoder()
        var bitmap = encoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, 200, 200)

        bitmap = removeQrcodeBackground(bitmap)
        return bitmap
    }


    private fun removeQrcodeBackground(bitmap: Bitmap):Bitmap{
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                if (bitmap.getPixel(x, y) == Color.WHITE) {
                    bitmap.setPixel(x, y, Color.TRANSPARENT)
                }
            }
        }
        return bitmap
    }
}