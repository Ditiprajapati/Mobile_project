package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import android.content.Intent

class TeacherProfileActivity : AppCompatActivity() {

    lateinit var tvName: TextView
    lateinit var tvUsername: TextView
    lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile)

        tvName = findViewById(R.id.tvName)
        tvUsername = findViewById(R.id.tvUsername)
        btnEdit = findViewById(R.id.btnEdit)

        // 🔥 GET REAL DATA
        val username = intent.getStringExtra("username")

        tvName.text = username   // You can later fetch full name
        tvUsername.text = "Username: $username"

        // ✏️ EDIT BUTTON

        btnEdit.setOnClickListener {
            val i = Intent(this, EditProfileActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
        }
    }
}