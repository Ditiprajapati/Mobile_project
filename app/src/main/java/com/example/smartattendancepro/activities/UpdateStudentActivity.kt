package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper

class UpdateStudentActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etRoll: EditText
    lateinit var etClass: EditText
    lateinit var btnUpdate: Button

    lateinit var dbHelper: DBHelper
    var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        etName = findViewById(R.id.etName)
        etRoll = findViewById(R.id.etRoll)
        etClass = findViewById(R.id.etClass)
        btnUpdate = findViewById(R.id.btnUpdate)

        dbHelper = DBHelper(this)

        studentId = intent.getStringExtra("id") ?: ""

        etName.setText(intent.getStringExtra("name"))
        etRoll.setText(intent.getStringExtra("roll"))
        etClass.setText(intent.getStringExtra("class"))

        btnUpdate.setOnClickListener {

            val success = dbHelper.updateStudent(
                studentId,
                etName.text.toString(),
                etRoll.text.toString(),
                etClass.text.toString()
            )

            if (success) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}