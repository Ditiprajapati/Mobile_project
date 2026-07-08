package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper

class UpdateTeacherActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnUpdate: Button

    lateinit var dbHelper: DBHelper
    var oldUsername: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_teacher)

        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        dbHelper = DBHelper(this)

        // 🔥 GET DATA FROM PREVIOUS SCREEN
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        oldUsername = username ?: ""

        // SET DATA
        etName.setText(name)
        etUsername.setText(username)
        etPassword.setText(password)

        btnUpdate.setOnClickListener {

            val newName = etName.text.toString()
            val newUsername = etUsername.text.toString()
            val newPassword = etPassword.text.toString()

            val success = dbHelper.updateTeacher(
                oldUsername,
                newName,
                newUsername,
                newPassword
            )

            if (success) {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}