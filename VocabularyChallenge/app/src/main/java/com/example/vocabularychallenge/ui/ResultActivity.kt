package com.example.vocabularychallenge.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.vocabularychallenge.R // 引入 R 檔案以使用顏色
import com.example.vocabularychallenge.databinding.ActivityResultBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.model.TestResult
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        // 1. 接收從 QuizActivity 傳來的資料
        val score = intent.getIntExtra("score", 0)
        val username = intent.getStringExtra("username") ?: "Guest"
        val unit = intent.getIntExtra("unit", 0)

        // 使用我們在 QuizActivity 中定義的新鍵名 "WRONG_WORD_IDS"
        val wrongWordIds = intent.getIntegerArrayListExtra("WRONG_WORD_IDS")

        // 顯示本次得分
        binding.tvScore.text = "$score 分"

        // 2. 分析與顯示歷史紀錄
        analyzeAndDisplayHistory(username, unit, score)

        setupButtons(unit, username, wrongWordIds)
    }

    private fun analyzeAndDisplayHistory(username: String, unit: Int, currentScore: Int) {
        // 從資料庫獲取這位使用者在這個單元的所有歷史紀錄 (已按時間排序)
        val history: List<TestResult> = db.getResultsForUserByUnit(username, unit)

        // --- 計算歷史平均分數 (包含本次) ---
        val allScores = history.map { it.score }
        val averageScore = allScores.average()
        binding.tvAverageValue.text = if (allScores.isNotEmpty()) {
            "${String.format("%.1f", averageScore)}分"
        } else {
            "${currentScore}分"
        }

        // --- 計算與上次的比較 ---
        if (history.size < 2) {
            binding.tvComparisonValue.text = "首次紀錄，繼續加油！"
            binding.tvComparisonValue.setTextColor(Color.BLACK)
        } else {
            val previousScore = history[history.size - 2].score
            val improvement = if (previousScore > 0) {
                ((currentScore.toDouble() - previousScore) / previousScore) * 100
            } else {
                if (currentScore > 0) 100.0 else 0.0
            }

            val comparisonText = when {
                improvement > 0 -> "進步 ${improvement.roundToInt()}%"
                improvement < 0 -> "退步 ${-improvement.roundToInt()}%"
                else -> "持平"
            }
            binding.tvComparisonValue.text = comparisonText
            binding.tvComparisonValue.setTextColor(
                when {
                    improvement > 0 -> ContextCompat.getColor(this, R.color.green_700)
                    improvement < 0 -> ContextCompat.getColor(this, R.color.red_700)
                    else -> Color.BLACK
                }
            )
        }

        setupLineChart(history)
    }

    private fun setupLineChart(history: List<TestResult>) {
        if (history.isEmpty()) {
            binding.lineChart.setNoDataText("暫無歷史成績可繪製圖表")
            binding.lineChart.invalidate()
            return
        }

        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        history.forEachIndexed { index, result ->
            entries.add(Entry(index.toFloat(), result.score.toFloat()))
            labels.add("第${index + 1}次")
        }

        val dataSet = LineDataSet(entries, "歷史成績").apply {
            color = ContextCompat.getColor(this@ResultActivity, R.color.purple_500)
            valueTextColor = Color.BLACK
            setCircleColor(ContextCompat.getColor(this@ResultActivity, R.color.purple_700))
            circleRadius = 4f
            lineWidth = 2.5f
            valueTextSize = 10f
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 105f
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
            invalidate()
        }
    }

    private fun setupButtons(unit: Int, username: String, wrongWordIds: ArrayList<Int>?) {
        binding.btnReview.setOnClickListener {

            android.util.Log.d("ReviewFlow", "--- 2. ResultActivity ---")
            android.util.Log.d("ReviewFlow", "點擊按鈕，從 Intent 拿到的錯題 ID 數量: ${wrongWordIds?.size}")
            android.util.Log.d("ReviewFlow", "錯題 ID 列表: $wrongWordIds")

            // 只有在錯題列表不為空且有內容時，才跳轉
            if (wrongWordIds != null && wrongWordIds.isNotEmpty()) {
                val i = Intent(this, VocabListActivity::class.java)
                // 將錯題列表傳遞給 VocabListActivity，並使用一個清晰的鍵名
                i.putIntegerArrayListExtra("REVIEW_WRONG_IDS", wrongWordIds)
                startActivity(i)
            } else {
                // 如果列表是空的或 null (代表全對)，給使用者一個提示
                Toast.makeText(this, "恭喜！本次測驗全對！", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAgain.setOnClickListener {
            val i = Intent(this, QuizActivity::class.java)
            i.putExtra("unit", unit)
            i.putExtra("username", username)
            startActivity(i)
            finish()
        }

        binding.btnMain.setOnClickListener {
            val i = Intent(this, MainMenuActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtra("username", username)
            startActivity(i)
            finish()
        }
    }
}
