package com.example.smartattendancepro.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper

class LoginActivity : AppCompatActivity() {

    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var spinnerRole: Spinner
    lateinit var btnLogin: Button
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        spinnerRole = findViewById(R.id.spinnerRole)
        btnLogin = findViewById(R.id.btnLogin)

        dbHelper = DBHelper(this)

        val roles = arrayOf("Principal", "Teacher")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRole.adapter = adapter

        btnLogin.setOnClickListener {

            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
           // val role = spinnerRole.selectedItem.toString()

           // val result = dbHelper.checkUser(username, password, role)

            val role = dbHelper.getUserRole(username, password)

            if (role != null) {

                if (role == "Teacher") {

                    val intent = Intent(this, TeacherDashboardActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)

                } else if (role == "Principal") {

                    val intent = Intent(this, PrincipalDashboardActivity::class.java)
                    startActivity(intent)
                }

            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}