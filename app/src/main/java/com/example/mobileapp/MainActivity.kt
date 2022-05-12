package com.example.mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mobileapp.models.UserInfo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {

    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient
    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHandler(this, null)
        users = dbHandler!!.listOfUserInfo()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (acct!=null) {
            var userExist : Boolean = false
            for (user in users!!) {
                if (user.email == acct.email) {
                    userExist = true
                    break
                }
            }
            if (!userExist) {
                dbHandler!!.insertData(acct.displayName.toString(), null.toString(), acct.email.toString())
            }
        }


    }

    fun signOut() {
        gsc.signOut().addOnCompleteListener {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}