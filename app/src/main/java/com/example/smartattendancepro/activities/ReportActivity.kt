package com.example.smartattendancepro.activities

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R
import com.example.smartattendancepro.database.DBHelper
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.utils.ColorTemplate
import java.io.File
import java.io.FileOutputStream

class ReportActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var tvTotalDays: TextView
    lateinit var tvPresent: TextView
    lateinit var tvAbsent: TextView
    lateinit var tvReportData: TextView
    lateinit var tvPercentage: TextView
    lateinit var pieChart: PieChart
    lateinit var btnDownload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        dbHelper = DBHelper(this)

        tvTotalDays = findViewById(R.id.tvTotalDays)
        tvPresent = findViewById(R.id.tvPresent)
        tvAbsent = findViewById(R.id.tvAbsent)
        tvReportData = findViewById(R.id.tvReportData)
        tvPercentage = findViewById(R.id.tvPercentage)
        pieChart = findViewById(R.id.pieChart)
        btnDownload = findViewById(R.id.btnDownload)

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attendance", null)

        var totalDays = 0
        var presentCount = 0
        var absentCount = 0

        val result = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(1)
                val data = cursor.getString(2)

                totalDays++

                result.append("📅 Date: $date\n")

                val lines = data.split("\n")

                for (line in lines) {
                    if (line.contains("Present")) {
                        presentCount++
                        result.append("✅ $line\n")
                    } else if (line.contains("Absent")) {
                        absentCount++
                        result.append("❌ $line\n")
                    }
                }

                result.append("\n----------------------\n\n")

            } while (cursor.moveToNext())
        }

        cursor.close()

        // ✅ Set Text Data
        tvTotalDays.text = "📅 Total Days: $totalDays"
        tvPresent.text = "✅ Total Present: $presentCount"
        tvAbsent.text = "❌ Total Absent: $absentCount"
        tvReportData.text = result.toString()

        // ✅ Percentage
        val total = presentCount + absentCount
        val percentage = if (total > 0) (presentCount * 100) / total else 0
        tvPercentage.text = "📈 Attendance %: $percentage%"

        // ✅ Pie Chart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(presentCount.toFloat(), "Present"))
        entries.add(PieEntry(absentCount.toFloat(), "Absent"))

        val dataSet = PieDataSet(entries, "Attendance")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()

        // ✅ Download PDF
        btnDownload.setOnClickListener {

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            val canvas = page.canvas
            val paint = Paint()

            var y = 25

            canvas.drawText("Attendance Report", 80f, y.toFloat(), paint)
            y += 25

            canvas.drawText("Total Days: $totalDays", 10f, y.toFloat(), paint)
            y += 20
            canvas.drawText("Present: $presentCount", 10f, y.toFloat(), paint)
            y += 20
            canvas.drawText("Absent: $absentCount", 10f, y.toFloat(), paint)
            y += 30

            val lines = result.toString().split("\n")
            for (line in lines) {
                canvas.drawText(line, 10f, y.toFloat(), paint)
                y += 15
            }

            pdfDocument.finishPage(page)

            val file = File(getExternalFilesDir(null), "AttendanceReport.pdf")
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            Toast.makeText(this, "PDF Saved: ${file.path}", Toast.LENGTH_LONG).show()
        }
    }
}