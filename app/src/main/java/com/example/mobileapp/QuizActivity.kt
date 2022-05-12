package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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

class QuizActivity : AppCompatActivity() {

    private val quizQuestions : Array<String> = Array(6) { "" }
    private val quizAnswers : Array<Array<String>> = Array(4) { Array(6) { "" } }
    private val quizScore : Array<Int> = Array(4) { 0 }
    private lateinit var radioAnswers : RadioGroup
    private lateinit var radioButton1 : RadioButton
    private lateinit var radioButton2 : RadioButton
    private lateinit var radioButton3 : RadioButton
    private  lateinit var radioButton4 : RadioButton
    private  lateinit var questionQuiz : TextView
    private   lateinit var numberQuiz : TextView
    private var currentQuestion : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val exitButton = findViewById<Button>(R.id.exit_quiz)
        exitButton.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        quizAnswers[0] = arrayOf(
            "А. Я хочу играть в кино или театре ",
            "А. Красочный и полный жизни ",
            "А. Банан",
            "А. Добрые песни",
            "А. В солнечную страну с пустынями",
            "А. Я изменю мир!")
        quizAnswers[1] = arrayOf(
            "Б. Мне кажется, я сбегу с бродячим цирком ",
            "Б. Разноцветный ",
            "Б. Клубника ",
            "Б. Танцевальную",
            "Б. Куда-нибудь, где люди добрые",
            "Б. Хочу рассмешить кого-то")
        quizAnswers[2] = arrayOf(
            "В. Я буду детективом ",
            "В. Крутой и хайповый ",
            "В. Голубика ",
            "В. Классическую",
            "В. К морю",
            "В. Я чувствую грусть ")
        quizAnswers[3] = arrayOf(
            "Г. Я могу быть человеком-оркестром и делать больше, чем одну вещь!",
            "Г. Каждый день ношу что-то разное",
            "Г. Зеленое яблоко",
            "Г. Мне нравится разная музыка, зависит от настроения",
            "Г. Везде! Я хочу объездить весь мир ",
            "Г. Чувствую себя, как всегда")

        quizQuestions[0] = "Кем ты хочешь стать, когда вырастешь?"
        quizQuestions[1] = "Какой у тебя стиль в одежде? "
        quizQuestions[2] = "Выбери что-то вкусное: "
        quizQuestions[3] = "Какую музыку ты слушаешь? "
        quizQuestions[4] = "Куда бы тебе хотелось поехать?"
        quizQuestions[5] = "Сегодня ты..."

        radioAnswers = findViewById(R.id.radiogrp)
        radioButton1 = findViewById(R.id.radioButton1)
        radioButton2 = findViewById(R.id.radioButton2)
        radioButton3 = findViewById(R.id.radioButton3)
        radioButton4 = findViewById(R.id.radioButton4)

        questionQuiz = findViewById(R.id.question_quiz)
        numberQuiz = findViewById(R.id.question_number)

        val buttonNextQuestion = findViewById<Button>(R.id.nextQuestionBtn)

        buttonNextQuestion.setOnClickListener {
            checkAnswer()
            radioAnswers.clearCheck()
        }

        initViews()
    }

    @SuppressLint("SetTextI18n")
    fun initViews() {
        if (currentQuestion+1 == 7) {
            finish()
            startActivity(Intent(this, QuizResultActivity::class.java))
        } else {
            radioButton1.text = quizAnswers[0][currentQuestion]
            radioButton2.text = quizAnswers[1][currentQuestion]
            radioButton3.text = quizAnswers[2][currentQuestion]
            radioButton4.text = quizAnswers[3][currentQuestion]

            questionQuiz.text = quizQuestions[currentQuestion]
            numberQuiz.text = (currentQuestion+1).toString() + "/6"
        }
    }

    private fun checkAnswer() {
        if (radioAnswers.checkedRadioButtonId != -1) {
            val checkedRadioButton = findViewById<RadioButton>(radioAnswers.checkedRadioButtonId)
            when (checkedRadioButton.text.toString()[0]) {
                'А' -> quizScore[0] ++
                'Б' -> quizScore[1] ++
                'В' -> quizScore[2] ++
                'Г' -> quizScore[3] ++
            }
            currentQuestion ++
            initViews()
        } else {
            Toast.makeText(this, "Choose a option", Toast.LENGTH_SHORT).show()
        }
    }
}