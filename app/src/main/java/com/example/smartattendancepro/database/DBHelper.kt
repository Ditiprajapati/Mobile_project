package com.example.smartattendancepro.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "SmartAttendanceDB", null, 2) {   // 🔥 VERSION 2

    override fun onCreate(db: SQLiteDatabase) {

        // USERS TABLE
        db.execSQL(
            "CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "username TEXT, " +
                    "password TEXT, " +
                    "role TEXT)"
        )

        // ATTENDANCE TABLE
        db.execSQL(
            "CREATE TABLE attendance(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date TEXT, " +
                    "data TEXT, " +
                    "image TEXT)"
        )

        db.execSQL(
            "CREATE TABLE students(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "roll TEXT, " +
                    "class TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS attendance")
        db.execSQL("DROP TABLE IF EXISTS students")// 🔥 FIXED
        onCreate(db)
    }

    // ---------------- USER FUNCTIONS ----------------

    fun insertUser(name: String, username: String, password: String, role: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("username", username)
        values.put("password", password)
        values.put("role", role)

        val result = db.insert("users", null, values)
        return result != -1L
    }

    fun checkUser(username: String, password: String, role: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=? AND role=?",
            arrayOf(username, password, role)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ---------------- ATTENDANCE FUNCTIONS ----------------

    fun insertAttendance(date: String, data: String, image: String) {
        val db = writableDatabase
        val values = ContentValues()

        values.put("date", date)
        values.put("data", data)
        values.put("image", image)

        db.insert("attendance", null, values)
    }

    fun getAllAttendance(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attendance", null)

        val result = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(1)
                val data = cursor.getString(2)

                result.append("Date: $date\n")
                result.append(data)
                result.append("\n-------------------\n")

            } while (cursor.moveToNext())
        }

        cursor.close()

        if (result.isEmpty()) {
            return "No attendance records found"
        }

        return result.toString()
    }


    fun updateUser(oldUsername: String, name: String, username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("username", username)
        values.put("password", password)

        val result = db.update("users", values, "username=?", arrayOf(oldUsername))
        return result > 0
    }


    fun getUser(username: String): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(username))
    }


    fun getUserRole(username: String, password: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT role FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )

        var role: String? = null

        if (cursor.moveToFirst()) {
            role = cursor.getString(0)
        }

        cursor.close()
        return role
    }

    fun insertStudent(name: String, roll: String, className: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("roll", roll)
        values.put("class", className)

        val result = db.insert("students", null, values)
        return result != -1L
    }

    fun getAllStudents(): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT name FROM students", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }

    fun getAllTeachers(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, username, password FROM users WHERE role='Teacher'", null)

        val result = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(0)
                val username = cursor.getString(1)
                val password = cursor.getString(2)

                result.append("👤 Name: $name\n")
                result.append("📧 Username: $username\n")
                result.append("🔒 Password: $password\n")
                result.append("----------------------\n")

            } while (cursor.moveToNext())
        }

        cursor.close()

        if (result.isEmpty()) return "No Teachers Found"

        return result.toString()
    }

    fun deleteTeacher(username: String): Boolean {
        val db = writableDatabase
        val result = db.delete("users", "username=?", arrayOf(username))
        return result > 0
    }

    fun updateTeacher(oldUsername: String, name: String, username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("username", username)
        values.put("password", password)

        val result = db.update("users", values, "username=?", arrayOf(oldUsername))
        return result > 0
    }


    fun getAllStudentsData(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT id, name, roll, class FROM students", null)
    }

    fun deleteStudent(id: String): Boolean {
        val db = writableDatabase
        val result = db.delete("students", "id=?", arrayOf(id))
        return result > 0
    }

    fun updateStudent(id: String, name: String, roll: String, className: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", name)
        values.put("roll", roll)
        values.put("class", className)

        val result = db.update("students", values, "id=?", arrayOf(id))
        return result > 0
    }
}