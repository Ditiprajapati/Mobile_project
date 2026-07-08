package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import android.content.Intent
import android.database.Cursor


class EditProfileActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnUpdate: Button

    lateinit var dbHelper: DBHelper
    var oldUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        dbHelper = DBHelper(this)

        // 🔥 GET DATA FROM PROFILE
        oldUsername = intent.getStringExtra("username")

        etUsername.setText(oldUsername)

        val cursor: Cursor = dbHelper.getUser(oldUsername!!)

        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(1))
            etUsername.setText(cursor.getString(2))
            etPassword.setText(cursor.getString(3))
        }

        cursor.close()

        btnUpdate.setOnClickListener {

            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()


            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.updateUser(oldUsername!!, name, username, password)

            if (success) {
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

                // 🔥 GO BACK WITH UPDATED DATA
                val intent = Intent(this, TeacherProfileActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}