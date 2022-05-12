package com.example.mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mobileapp.fragments.GamesFragment
import com.example.mobileapp.fragments.HomeFragment
import com.example.mobileapp.fragments.ProfileFragment
import com.example.mobileapp.models.UserInfo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken

class MainActivity : AppCompatActivity() {

    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient
    lateinit var acct : GoogleSignInAccount
    var dbHandler: DatabaseHandler? = null
    var users: ArrayList<UserInfo>? = null
    lateinit var VKAccessTkn : VKAccessToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHandler(this, null)
        users = dbHandler!!.listOfUserInfo()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        val homeFragment = HomeFragment()
        val gamesFragment = GamesFragment()
        val profileFragment = ProfileFragment()

        makeCurrentFragment(homeFragment)

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

        /**
        val buttonSignOut = findViewById<Button>(R.id.sign_out)

        buttonSignOut.setOnClickListener {
            signOut()
        }
        **/

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.games -> makeCurrentFragment(gamesFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)
            }
            true
        }
    }

    fun signOut() {
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (VK.isLoggedIn()) {
            VK.logout()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        } else if (acct != null){
            gsc.signOut().addOnCompleteListener {
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        } else {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun makeCurrentFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}