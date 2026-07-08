package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import android.content.Intent

class AddStudentActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etRoll: EditText
    lateinit var etClass: EditText
    lateinit var btnAdd: Button

    lateinit var btnViewStudents: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        etName = findViewById(R.id.etName)
        etRoll = findViewById(R.id.etRoll)
        etClass = findViewById(R.id.etClass)
        btnAdd = findViewById(R.id.btnAdd)

        val dbHelper = DBHelper(this)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnViewStudents = findViewById(R.id.btnViewStudents) // 🔥 THIS WAS MISSING

        btnViewStudents.setOnClickListener {
            startActivity(Intent(this, ViewStudentsActivity::class.java))
        }

        btnAdd.setOnClickListener {

            val name = etName.text.toString()
            val roll = etRoll.text.toString()
            val className = etClass.text.toString()

            if (name.isEmpty() || roll.isEmpty() || className.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
            } else {

                val success = dbHelper.insertStudent(name, roll, className)

                if (success) {
                    Toast.makeText(this, "Student Added Successfully", Toast.LENGTH_SHORT).show()

                    etName.text.clear()
                    etRoll.text.clear()
                    etClass.text.clear()
                } else {
                    Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}