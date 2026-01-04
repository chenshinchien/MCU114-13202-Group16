package com.example.vocabularychallenge.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularychallenge.databinding.ActivityMainMenuBinding
import android.widget.ArrayAdapter


class MainMenuActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private var username: String = "guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)



        username = intent.getStringExtra("username") ?: "guest"
        binding.tvWelcome.text = "歡迎：$username"

        binding.btnVocabBrowse.setOnClickListener {
            val i = Intent(this, VocabListActivity::class.java)
            startActivity(i)
        }

        binding.btnQuizUnit1.setOnClickListener {
            startQuiz(1)
        }
        binding.btnQuizUnit2.setOnClickListener {
            startQuiz(2)
        }
        binding.btnQuizAll.setOnClickListener {
            startQuiz(0)
        }

        binding.btnRecords.setOnClickListener {
            val i = Intent(this, RecordsActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
        }

        binding.btnFeedback.setOnClickListener {
            val i = Intent(this, FeedbackActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
        }

        binding.btnLogout.setOnClickListener {
            // stop service
            val serviceIntent =
                Intent(this, com.example.vocabularychallenge.service.UsageService::class.java)
            stopService(serviceIntent)

            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }

    private fun startQuiz(unit: Int) {
        val i = Intent(this, QuizActivity::class.java)
        i.putExtra("unit", unit)
        i.putExtra("username", username)
        startActivity(i)
    }
}
