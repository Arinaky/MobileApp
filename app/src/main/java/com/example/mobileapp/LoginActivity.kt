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
        val buttonVK = findViewById<ImageView>(R.id.vk_btn)

        val test = findViewById<TextView>(R.id.test)
        val fingerprints = getCertificateFingerprint(this, this.packageName)
        test.text = fingerprints!![0]

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (acct!=null) {
            toMainPage()
        }

        if (VK.isLoggedIn()) {
            toMainPage()
        }

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

        buttonVK.setOnClickListener {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
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
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                toMainPage()
                var userExist : Boolean = false
                for (user in users!!) {
                    if (user.username == token.userId.toString()) {
                        userExist = true
                        break
                    }
                }
                if (!userExist) {
                    dbHandler!!.insertData(token.userId.toString(), null.toString(), token.email.toString())
                }
            }
            override fun onLoginFailed(authException: VKAuthException) {
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}