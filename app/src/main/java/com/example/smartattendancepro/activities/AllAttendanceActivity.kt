package com.example.smartattendancepro.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper

class AllAttendanceActivity : AppCompatActivity() {

    lateinit var tvAllData: TextView
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_attendance)

        tvAllData = findViewById(R.id.tvAllData)
        dbHelper = DBHelper(this)

        val data = dbHelper.getAllAttendance()
        tvAllData.text = data
    }

    private fun stringToBitmap(encoded: String): Bitmap {
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}