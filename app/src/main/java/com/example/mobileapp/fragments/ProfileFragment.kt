package com.example.mobileapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mobileapp.LoginActivity
import com.example.mobileapp.R
import com.example.mobileapp.RegistrationActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.vk.api.sdk.VK

class ProfileFragment : Fragment() {

    lateinit var gsc : GoogleSignInClient
    lateinit var gso : GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonSignOut = view.findViewById<Button>(R.id.sign_out)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        buttonSignOut.setOnClickListener {
            signOut()
        }
        return view
    }
    fun signOut() {
        val acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (VK.isLoggedIn()) {
            VK.logout()
            activity?.finish()
            val intent = Intent (getActivity(), RegistrationActivity::class.java)
            getActivity()?.startActivity(intent)
        } else if (acct != null){
            gsc.signOut().addOnCompleteListener {
                activity?.finish()
                val intent = Intent (getActivity(), RegistrationActivity::class.java)
                getActivity()?.startActivity(intent)
            }
        } else {
            activity?.finish()
            val intent = Intent (getActivity(), RegistrationActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }
}