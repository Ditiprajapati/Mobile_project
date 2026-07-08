package com.example.smartattendancepro.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper

class ViewStudentsActivity : AppCompatActivity() {

    lateinit var studentContainer: LinearLayout
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_students)

        studentContainer = findViewById(R.id.studentContainer)
        dbHelper = DBHelper(this)

        loadStudents()
    }

    private fun loadStudents() {

        studentContainer.removeAllViews()

        val cursor = dbHelper.getAllStudentsData()

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val roll = cursor.getString(2)
                val className = cursor.getString(3)

                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(20, 20, 20, 20)
                layout.setBackgroundColor(android.graphics.Color.WHITE)

                val tv = TextView(this)
                tv.text = "👤 $name\n🎫 Roll: $roll\n🏫 Class: $className"
                tv.textSize = 16f

                val btnLayout = LinearLayout(this)
                btnLayout.orientation = LinearLayout.HORIZONTAL

                val btnUpdate = Button(this)
                btnUpdate.text = "Update"

                val btnDelete = Button(this)
                btnDelete.text = "Delete"

                // DELETE
                btnDelete.setOnClickListener {
                    val success = dbHelper.deleteStudent(id)
                    if (success) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                        loadStudents()
                    }
                }

                // UPDATE
                btnUpdate.setOnClickListener {
                    val intent = Intent(this, UpdateStudentActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("name", name)
                    intent.putExtra("roll", roll)
                    intent.putExtra("class", className)
                    startActivity(intent)
                }

                btnLayout.addView(btnUpdate)
                btnLayout.addView(btnDelete)

                layout.addView(tv)
                layout.addView(btnLayout)

                val space = Space(this)
                space.minimumHeight = 20

                studentContainer.addView(layout)
                studentContainer.addView(space)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }
}