package com.example.smartattendancepro.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.Toolbar
import com.example.smartattendancepro.R

class PrincipalDashboardActivity : AppCompatActivity() {

    lateinit var cardAddStudent: CardView
    lateinit var cardAddTeacher: CardView
    lateinit var cardViewAttendance: CardView
    lateinit var cardReport: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_dashboard)

        // 🔗 Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 🔗 Cards
        cardAddStudent = findViewById(R.id.cardAddStudent)
        cardAddTeacher = findViewById(R.id.cardAddTeacher)
        cardViewAttendance = findViewById(R.id.cardViewAttendance)
        cardReport = findViewById(R.id.cardReport)

        // 👨‍🎓 Add Student
        cardAddStudent.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }

        // 👨‍🏫 Add Teacher
        cardAddTeacher.setOnClickListener {
            startActivity(Intent(this, AddTeacherActivity::class.java))
        }

        // 📊 View Attendance
        cardViewAttendance.setOnClickListener {
            startActivity(Intent(this, AllAttendanceActivity::class.java))
        }

        // 📈 Report
        cardReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

        cardViewAttendance.setOnClickListener {
            startActivity(Intent(this, AllAttendanceActivity::class.java))
        }
    }

    // 🔥 MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    // 🔥 LOGOUT ACTION
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_logout -> {

                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure you want to logout?")

                builder.setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.show()
            }
        }
        return true
    }
}