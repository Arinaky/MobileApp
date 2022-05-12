package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
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

class LoginActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null
    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient
    lateinit var buttonGoogle : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)

        val buttonLogin = findViewById<Button>(R.id.button_login)
        val buttonRegistration = findViewById<Button>(R.id.button_registration)
        val buttonGoogle = findViewById<ImageView>(R.id.google_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)

        buttonLogin.setOnClickListener {
            dbHandler = DatabaseHandler(this, null)
            users = dbHandler!!.listOfUserInfo()
            var errorUsername : Boolean = false
            var errorPassword : Boolean = false
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
            intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        buttonGoogle.setOnClickListener {
            googleSignIn()
        }
    }
    fun googleSignIn() {
        val signInIntent : Intent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    fun toMainPage() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) run {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)
                toMainPage()
            } catch (exception : ApiException) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}