package com.example.mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mobileapp.FieldValidators.isStringContainNumber
import com.example.mobileapp.FieldValidators.isStringLowerAndUpperCase
import com.example.mobileapp.models.UserInfo
import com.example.mobileapp.FieldValidators.isValidEmail
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

class RegistrationActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null
    private var email: TextView? = null
    private var username: TextView? = null
    private var password: TextView? = null
    private var confirmPassword: TextView? = null
    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient
    lateinit var buttonGoogle : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        email = findViewById<TextView>(R.id.email)
        username = findViewById<TextView>(R.id.username)
        password = findViewById<TextView>(R.id.password)
        confirmPassword = findViewById<TextView>(R.id.confirm_password)

        val buttonSignUp = findViewById<Button>(R.id.button_sign_up)
        val buttonBack = findViewById<Button>(R.id.button_back_to_sign_in)

        buttonBack.setOnClickListener {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        buttonSignUp.setOnClickListener {
            if (isValidate()) {
                val usernameTry = username
                val passwordTry = password
                val emailTry = email
                dbHandler = DatabaseHandler(this, null)
                users = dbHandler!!.listOfUserInfo()
                var error : Boolean = false
                for (user in users!!) {
                    if (user.username == usernameTry?.text.toString()) {
                        Toast.makeText(this, "Registration fail. Such username already exists!", Toast.LENGTH_SHORT).show()
                        error = true
                        break
                    } else if (user.email == emailTry?.text.toString()) {
                        Toast.makeText(this, "Registration fail. Account with this email already exists!", Toast.LENGTH_SHORT).show()
                        error = true
                        break
                    }
                }
                if (!error) {
                    Toast.makeText(this, "Registration success!", Toast.LENGTH_SHORT).show()
                    dbHandler!!.insertData(usernameTry?.text.toString(), passwordTry?.text.toString(), emailTry?.text.toString())
                    finish()
                }
            }
        }
        val buttonGoogle = findViewById<ImageView>(R.id.google_btn)
        val buttonVK = findViewById<ImageView>(R.id.vk_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (acct!=null) {
            toMainPage()
        }

        if (VK.isLoggedIn()) {
            toMainPage()
        }

        buttonGoogle.setOnClickListener {
            googleSignIn()
        }

        buttonVK.setOnClickListener {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
        }
    }

    fun isValidate() : Boolean {
        return validateEmail() && validateUsername() && validatePassword() && validateConfirm_password()
    }

    fun validateEmail() : Boolean {
        if (email!!.text.toString().trim().isEmpty()) {
            email!!.error = "Required Field!"
            email!!.requestFocus()
            return false
        } else if (!isValidEmail(email!!.text.toString())) {
            email!!.error = "Invalid Email!"
            email!!.requestFocus()
            return false
        } else {
            email!!.error = null
        }
        return true
    }

    fun validateUsername() : Boolean {
        if (username!!.text.toString().trim().isEmpty()) {
            username!!.error = "Required Field!"
            username!!.requestFocus()
            return false
        } else {
            username!!.error = null
        }
        return true
    }

    fun validatePassword() : Boolean {
        if (password!!.text.toString().trim().isEmpty()) {
            password!!.error = "Required Field!"
            password!!.requestFocus()
            return false
        } else if (password!!.text.toString().length < 6) {
            password!!.error = "password can't be less than 6"
            password!!.requestFocus()
            return false
        } else if (!isStringContainNumber(password!!.text.toString())) {
            password!!.error = "Required at least 1 digit"
            password!!.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(password!!.text.toString())) {
            password!!.error = "Password must contain upper and lower case letters"
            password!!.requestFocus()
            return false
        }  else {
            password!!.error = null
        }
        return true
    }

    fun validateConfirm_password() : Boolean {
        when {
            confirmPassword!!.text.toString().trim().isEmpty() -> {
                confirmPassword!!.error = "Required Field!"
                confirmPassword!!.requestFocus()
                return false
            }
            confirmPassword!!.text.toString() != password!!.text.toString() -> {
                confirmPassword!!.error = "Passwords don't match"
                confirmPassword!!.requestFocus()
                return false
            }
            else -> {
                confirmPassword!!.error = null
            }
        }
        return true
    }

    fun toMainPage() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun googleSignIn() {
        val signInIntent : Intent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) run {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                toMainPage()
            } catch (exception : ApiException) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                toMainPage()
                Toast.makeText(this@RegistrationActivity, "Login Success", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@RegistrationActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            when (view.id) {
                R.id.email -> {validateEmail()}
                R.id.username -> {validateUsername()}
                R.id.password -> {validatePassword()}
                R.id.confirm_password -> {validateConfirm_password()}
            }
        }
    }
}