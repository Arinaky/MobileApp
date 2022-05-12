package com.example.mobileapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mobileapp.QuizActivity
import com.example.mobileapp.R

class GamesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_games, container, false)
        val buttonStartQuiz = view.findViewById<Button>(R.id.button_quiz)
        buttonStartQuiz.setOnClickListener {
            activity?.finish()
            val intent = Intent (activity, QuizActivity::class.java)
            activity?.startActivity(intent)
        }
        return view
    }
}