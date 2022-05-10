package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobileapp.models.UserInfo

class DatabaseHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME,  factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 1
        const val ID = "ID"
        const val USERNAME = "USERNAME"
        const val PASSWORD = "PASSWORD"
        const val EMAIL = "EMAIL"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table Users (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, EMAIL TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun insertData(username: String, password: String, email: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USERNAME, username)
        contentValues.put(PASSWORD, password)
        contentValues.put(EMAIL, email)
        db.insert("Users", null, contentValues)
    }

    fun updateData(id: String, username: String, password: String, email: String) : Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put(USERNAME, username)
        contentValues.put(PASSWORD, password)
        contentValues.put(EMAIL, email)
        db.update("Users", contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun deleteData(id: String) : Int {
        val db = this.writableDatabase
        return db.delete("Users", "ID = ?", arrayOf(id))
    }

    @SuppressLint("Recycle")
    fun listOfUserInfo() : ArrayList<UserInfo> {
        val db = this.writableDatabase
        val res = db.rawQuery("select * from Users;", null)
        val UsersList = ArrayList<UserInfo>()
        while (res.moveToNext()) {
            val userInfo = UserInfo()
            userInfo.id = Integer.valueOf(res.getString(0))
            userInfo.username = res.getString(1)
            userInfo.password = res.getString(2)
            userInfo.email = res.getString(3)
            UsersList.add(userInfo)
        }
        return UsersList
    }
}