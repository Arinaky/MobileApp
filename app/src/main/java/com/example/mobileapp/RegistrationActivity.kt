package com.example.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val email = findViewById<TextView>(R.id.email)
        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)

        val buttonSignUp = findViewById<Button>(R.id.button_sign_up)
        val buttonBack = findViewById<Button>(R.id.button_back_to_sign_in)

        buttonBack.setOnClickListener {
            finish()
        }
    }
}