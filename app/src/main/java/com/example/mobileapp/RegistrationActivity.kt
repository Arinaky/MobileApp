package com.example.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mobileapp.FieldValidators.isStringContainNumber
import com.example.mobileapp.FieldValidators.isStringLowerAndUpperCase
import com.example.mobileapp.models.UserInfo
import com.example.mobileapp.FieldValidators.isValidEmail

class RegistrationActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null
    private var email: TextView? = null
    private var username: TextView? = null
    private var password: TextView? = null
    private var confirmPassword: TextView? = null

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