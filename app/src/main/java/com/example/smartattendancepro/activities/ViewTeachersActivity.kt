package com.example.smartattendancepro.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import android.content.Intent

class ViewTeachersActivity : AppCompatActivity() {

    lateinit var teacherContainer: LinearLayout
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_teachers)

        teacherContainer = findViewById(R.id.teacherContainer)
        dbHelper = DBHelper(this)

        loadTeachers()
    }

    private fun loadTeachers() {

        teacherContainer.removeAllViews()

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT name, username, password FROM users WHERE role='Teacher'",
            null
        )

        if (cursor.moveToFirst()) {
            do {

                val name = cursor.getString(0)
                val username = cursor.getString(1)
                val password = cursor.getString(2)

                // 🔥 CARD LAYOUT
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(20, 20, 20, 20)
                layout.setBackgroundColor(android.graphics.Color.WHITE)

                val tv = TextView(this)
                tv.text = "👤 $name\n📧 $username\n🔒 $password"
                tv.textSize = 16f

                // BUTTON LAYOUT
                val btnLayout = LinearLayout(this)
                btnLayout.orientation = LinearLayout.HORIZONTAL

                val btnUpdate = Button(this)
                btnUpdate.text = "Update"

                val btnDelete = Button(this)
                btnDelete.text = "Delete"

                // DELETE ACTION
                btnDelete.setOnClickListener {

                    val success = dbHelper.deleteTeacher(username)

                    if (success) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                        loadTeachers() // 🔄 refresh
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

                // UPDATE (for now just toast)
                btnUpdate.setOnClickListener {

                    val intent = Intent(this, UpdateTeacherActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("username", username)
                    intent.putExtra("password", password)

                    startActivity(intent)
                }

                btnLayout.addView(btnUpdate)
                btnLayout.addView(btnDelete)

                layout.addView(tv)
                layout.addView(btnLayout)

                // spacing
                val space = Space(this)
                space.minimumHeight = 20

                teacherContainer.addView(layout)
                teacherContainer.addView(space)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }
}