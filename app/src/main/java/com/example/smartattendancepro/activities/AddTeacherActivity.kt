package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import android.content.Intent

class AddTeacherActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnAddTeacher: Button
    lateinit var btnViewTeachers: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)

        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnAddTeacher = findViewById(R.id.btnAddTeacher)

        val dbHelper = DBHelper(this)

        btnAddTeacher.setOnClickListener {

            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {

                val success = dbHelper.insertUser(
                    name,
                    username,
                    password,
                    "Teacher"   // 🔥 IMPORTANT
                )

                if (success) {
                    Toast.makeText(this, "Teacher Added Successfully", Toast.LENGTH_SHORT).show()

                    etName.text.clear()
                    etUsername.text.clear()
                    etPassword.text.clear()

                } else {
                    Toast.makeText(this, "Error adding teacher", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnViewTeachers = findViewById(R.id.btnViewTeachers)

        btnViewTeachers.setOnClickListener {
            startActivity(Intent(this, ViewTeachersActivity::class.java))
        }
    }
}