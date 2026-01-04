package com.example.vocabularychallenge.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularychallenge.databinding.ActivityQuizBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.model.Vocab
import kotlin.random.Random
import android.content.IntentFilter
import com.example.vocabularychallenge.receiver.CountdownReceiver
import android.content.Context.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat


class QuizActivity: AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var db: DatabaseHelper
    private var unit: Int = 0
    private var username: String = "guest"
    private var questionList = mutableListOf<Vocab>()
    private var index = 0
    private var score = 0
    private var totalQuestions = 5
    private var timer: CountDownTimer? = null
    private val countdownReceiver = CountdownReceiver()
    private var startTime: Long = 0L

    private val wrongVocabIds = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        unit = intent.getIntExtra("unit", 0)
        username = intent.getStringExtra("username") ?: "guest"

        // 從資料庫取得單字列表並隨機化
        val allVocabs = db.getVocabList(unit).shuffled()
        totalQuestions = if (allVocabs.size < 5) allVocabs.size else 5
        questionList = allVocabs.take(totalQuestions).toMutableList()


        binding.tvTitle.text = "單元 ${if(unit==0) "全部" else unit} 測驗"
        startTime = System.currentTimeMillis()
        showQuestion()

        // 註冊廣播接收器
        ContextCompat.registerReceiver(
            this,
            countdownReceiver,
            IntentFilter("com.example.vocabularychallenge.COUNTDOWN"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        binding.btnChoice1.setOnClickListener { onChoiceSelected(binding.btnChoice1.text.toString()) }
        binding.btnChoice2.setOnClickListener { onChoiceSelected(binding.btnChoice2.text.toString()) }
        binding.btnChoice3.setOnClickListener { onChoiceSelected(binding.btnChoice3.text.toString()) }
        binding.btnChoice4.setOnClickListener { onChoiceSelected(binding.btnChoice4.text.toString()) }
    }

    private fun showQuestion() {
        if (index >= totalQuestions) {
            endQuiz()
            return
        }
        val q = questionList[index]
        binding.tvQnum.text = "${index+1} / $totalQuestions"
        binding.tvQuestion.text = "以下何者為 \"${q.english}\" 的意思？"

        // 準備選項（正確 + 3 個干擾）
        val pool = db.getVocabList(0).map { it.chinese }.toMutableList()
        pool.remove(q.chinese)
        pool.shuffle()
        val options = mutableListOf(q.chinese) // 先加入正確答案
        options.addAll(pool.take(3))
        options.shuffle()

        binding.btnChoice1.text = options[0]
        binding.btnChoice2.text = options[1]
        binding.btnChoice3.text = options[2]
        binding.btnChoice4.text = options[3]

        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        binding.tvTimer.text = "5"
        binding.progressBar.max = 5
        binding.progressBar.progress = 5
        timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val s = (millisUntilFinished/1000).toInt()
                binding.tvTimer.text = s.toString()
                binding.progressBar.progress = s
            }
            override fun onFinish() {
                binding.tvTimer.text = "0"
                binding.progressBar.progress = 0
                // send broadcast to receiver
                val i = Intent("com.example.vocabularychallenge.COUNTDOWN")
                i.putExtra("msg", "時間到！")
                i.setPackage(packageName)
                sendBroadcast(i)

                val currentQuestion = questionList[index]
                wrongVocabIds.add(currentQuestion.id)

                // treat as wrong and go next
                index++
                showQuestion()
            }
        }.start()
    }

    private fun onChoiceSelected(choice: String) {
        timer?.cancel()
        val currentQuestion = questionList[index]
        val correct = currentQuestion.chinese

        if (choice == correct) {
            score += (100 / totalQuestions) // 根據總題數計算每題分數
        } else {
            wrongVocabIds.add(currentQuestion.id)
        }
        index++
        showQuestion()
    }

    private fun endQuiz() {
        timer?.cancel()

        // 計算總花費時間 (秒)
        val timeSpentInMillis = System.currentTimeMillis() - startTime
        val timeSpentInSeconds = (timeSpentInMillis / 1000).toInt()

        // 取得當前時間字串
        val now = java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        db.recordResult(username, unit, score, timeSpentInSeconds, now)

        // 建立 Intent 並放入所有需要的資料
        val i = Intent(this, ResultActivity::class.java)
        i.putExtra("score", score)
        i.putExtra("username", username)
        i.putExtra("unit", unit)
        i.putExtra("timeSpent", timeSpentInSeconds)

        // 我們使用一個新的、更清晰的鍵名 "WRONG_WORD_IDS"
        i.putIntegerArrayListExtra("WRONG_WORD_IDS", ArrayList(wrongVocabIds))

        android.util.Log.d("ReviewFlow", "--- 1. QuizActivity ---")
        android.util.Log.d("ReviewFlow", "測驗結束，準備發送的錯題 ID 數量: ${wrongVocabIds.size}")
        android.util.Log.d("ReviewFlow", "錯題 ID 列表: $wrongVocabIds")

        startActivity(i)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        unregisterReceiver(countdownReceiver)
    }
}
