package com.example.smartattendancepro.activities

import android.content.Intent
import android.os.Bundle

import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import androidx.cardview.widget.CardView

class TeacherDashboardActivity : AppCompatActivity() {

    lateinit var cardAttendance: CardView
    lateinit var cardView: CardView
    lateinit var cardProfile: CardView
    lateinit var menuIcon: ImageView   // 🔥 3 dots

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        // ✅ INIT
        cardAttendance = findViewById(R.id.cardAttendance)
        cardView = findViewById(R.id.cardView)
        cardProfile = findViewById(R.id.cardProfile)
        menuIcon = findViewById(R.id.menuIcon)

        val username = intent.getStringExtra("username")

        // 📸 Attendance
        cardAttendance.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }

        // 📊 View Data
        cardView.setOnClickListener {
            startActivity(Intent(this, AllAttendanceActivity::class.java))
        }

        // 👤 Profile
        cardProfile.setOnClickListener {
            val intent = Intent(this, TeacherProfileActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        val cardReport = findViewById<CardView>(R.id.cardReport)

        cardReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

        // 🔥 3 DOT MENU (CORRECT PLACE)
        menuIcon.setOnClickListener {
            val popup = PopupMenu(this, menuIcon)
            popup.menuInflater.inflate(R.menu.menu_dashboard, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.menu_logout -> {
                        showLogoutDialog()
                        true
                    }

                    else -> false
                }
            }

            popup.show()
        }
    }

    // 🔥 LOGOUT FUNCTION
    private fun showLogoutDialog() {
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