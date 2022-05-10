package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.models.UserInfo

class LoginActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)

        val buttonLogin = findViewById<Button>(R.id.button_login)
        val buttonRegistration = findViewById<Button>(R.id.button_registration)

        buttonLogin.setOnClickListener {
            dbHandler = DatabaseHandler(this, null)
            dbHandler!!.insertData("admin", "admin", "admin@admin.com")
            users = dbHandler!!.listOfUserInfo()
            for (user in users!!) {
                if (username.text.toString() == user.username) {
                    if (password.text.toString() == user.password) {
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Fail. Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login Fail. No such username", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}