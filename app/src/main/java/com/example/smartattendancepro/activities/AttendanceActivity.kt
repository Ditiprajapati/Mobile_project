package com.example.smartattendancepro.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import java.io.ByteArrayOutputStream
import android.util.Base64

class AttendanceActivity : AppCompatActivity() {

    lateinit var btnCamera: Button
    lateinit var btnSave: Button
    lateinit var imageView: ImageView
    lateinit var mediaPlayer: MediaPlayer

    lateinit var tvDate: TextView
    lateinit var btnDate: Button

    lateinit var cbRashi: CheckBox
    lateinit var cbVinita: CheckBox
    lateinit var cbNidhi: CheckBox
    lateinit var cbAnsh: CheckBox
    lateinit var cbDhruv: CheckBox
    lateinit var cbDharmik: CheckBox
    //lateinit var attendanceContainer: LinearLayout

    lateinit var studentContainer: LinearLayout
    val checkBoxList = ArrayList<CheckBox>()
    val CHANNEL_ID = "attendance_channel"
    var capturedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        btnCamera = findViewById(R.id.btnCamera)
        btnSave = findViewById(R.id.btnSave)
        imageView = findViewById(R.id.imageView)

        tvDate = findViewById(R.id.tvDate)
        btnDate = findViewById(R.id.btnDate)


        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        createNotificationChannel()

        // 📸 CAMERA
        btnCamera.setOnClickListener {
            checkCameraPermission()
        }

        // 📅 DATE
        btnDate.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()

            val datePickerDialog = android.app.DatePickerDialog(
                this,
                { _, y, m, d ->
                    val date = "$d/${m + 1}/$y"
                    tvDate.text = "Selected Date: $date"
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // 💾 SAVE
        btnSave.setOnClickListener {

            val result = StringBuilder()


            for (checkBox in checkBoxList) {
                val name = checkBox.text.toString()

                if (checkBox.isChecked) {
                    result.append("$name: Present\n")
                } else {
                    result.append("$name: Absent\n")
                }
            }

            AlertDialog.Builder(this)
                .setTitle("Save Attendance")
                .setMessage("Do you want to save attendance?")
                .setPositiveButton("Yes") { _, _ ->

                    playSound()
                    showNotification()

                    val imageString = if (capturedBitmap != null)
                        bitmapToString(capturedBitmap!!)
                    else ""

                    val dbHelper = DBHelper(this)
                    dbHelper.insertAttendance(
                        tvDate.text.toString(),
                        result.toString(),
                        imageString
                    )

                    val intent = Intent(this, ViewDataActivity::class.java)
                    intent.putExtra("attendanceData", result.toString())
                    intent.putExtra("date", tvDate.text.toString())
                    intent.putExtra("image", imageString)
                    startActivity(intent)

                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        studentContainer = findViewById(R.id.studentContainer)

        val dbHelper = DBHelper(this)
        val students = dbHelper.getAllStudents()

        for (name in students) {

            val checkBox = CheckBox(this)
            checkBox.text = name
            checkBox.textSize = 18f

            studentContainer.addView(checkBox)
            checkBoxList.add(checkBox)
        }

       //attendanceContainer = findViewById(R.id.attendanceContainer)

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT date, data, image FROM attendance", null)

        if (cursor.moveToFirst()) {
            do {

                val date = cursor.getString(0)
                val data = cursor.getString(1)
                val imageString = cursor.getString(2)

                // 🔥 CARD
                val card = androidx.cardview.widget.CardView(this)
                card.radius = 20f
                card.cardElevation = 8f

                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(20, 20, 20, 20)

                // DATE
                val tvDate = TextView(this)
                tvDate.text = "📅 Date: $date"
                tvDate.textSize = 18f
                tvDate.setTypeface(null, android.graphics.Typeface.BOLD)

                // DATA
                val tvData = TextView(this)
                tvData.text = data
                tvData.textSize = 16f

                // IMAGE
                val imageView = ImageView(this)
                imageView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    400
                )

                if (!imageString.isNullOrEmpty()) {
                    val bitmap = stringToBitmap(imageString)
                    imageView.setImageBitmap(bitmap)
                }

                layout.addView(tvDate)
                layout.addView(tvData)
                layout.addView(imageView)

                card.addView(layout)

                val space = Space(this)
                space.minimumHeight = 20

                //attendanceContainer.addView(card)
                //attendanceContainer.addView(space)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    private fun stringToBitmap(encoded: String): Bitmap {
        val bytes = android.util.Base64.decode(encoded, android.util.Base64.DEFAULT)
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    // 📸 CAMERA RESULT
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            capturedBitmap = bitmap
            imageView.setImageBitmap(bitmap)
        }
    }

    // 🔊 SOUND
    private fun playSound() {
        mediaPlayer.start()
    }

    // 🔔 NOTIFICATION
    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Attendance")
            .setContentText("Attendance saved successfully")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Attendance Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    // 🔐 CAMERA PERMISSION
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                101
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            openCamera()
        }
    }

    // 🔄 BITMAP → STRING
    private fun bitmapToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}