package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.models.UserInfo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException
import com.vk.api.sdk.requests.VKRequest
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint


class LoginActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var buttonGoogle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)

        val buttonLogin = findViewById<Button>(R.id.button_login)
        val buttonRegistration = findViewById<Button>(R.id.button_registration)

        buttonLogin.setOnClickListener {
            dbHandler = DatabaseHandler(this, null)
            users = dbHandler!!.listOfUserInfo()
            var errorUsername: Boolean = false
            var errorPassword: Boolean = false
            for (user in users!!) {
                if (username.text.toString() == user.username) {
                    if (password.text.toString() == user.password) {
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        errorUsername = false
                        errorPassword = false
                        toMainPage()
                        break
                    } else {
                        errorPassword = true
                        break
                    }
                } else {
                    errorUsername = true
                }
            }
            if (errorPassword) {
                Toast.makeText(this, "Login Fail. Incorrect password", Toast.LENGTH_SHORT).show()
            } else if (errorUsername) {
                Toast.makeText(this, "Login Fail. No such username", Toast.LENGTH_SHORT).show()
            }
        }

        buttonRegistration.setOnClickListener {
            finish()
            intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
    fun toMainPage() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}