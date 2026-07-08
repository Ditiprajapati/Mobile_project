package com.example.smartattendancepro.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R

class ViewDataActivity : AppCompatActivity() {

    lateinit var tvDate: TextView
    lateinit var tvData: TextView
    lateinit var imgPhoto: ImageView   // 🔥 NEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        tvDate = findViewById(R.id.tvDate)
        tvData = findViewById(R.id.tvData)
        imgPhoto = findViewById(R.id.imgPhoto)   // 🔥 NEW

        val date = intent.getStringExtra("date")
        val data = intent.getStringExtra("attendanceData")
        val imageString = intent.getStringExtra("image")   // 🔥 NEW

        tvDate.text = date
        tvData.text = data

        // 🔥 SHOW IMAGE
        if (imageString != null) {
            imgPhoto.setImageBitmap(stringToBitmap(imageString))
        }
    }

    private fun stringToBitmap(encoded: String): Bitmap {
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}