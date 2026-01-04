package com.example.vocabularychallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularychallenge.databinding.ActivityFeedbackBinding
import android.widget.Toast
import com.example.vocabularychallenge.db.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FeedbackActivity: AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var db: DatabaseHelper

    private var currentUsername: String = "guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // 從 Intent 中取得從上個頁面傳來的使用者名稱
        currentUsername = intent.getStringExtra("username") ?: "guest"


        binding.btnSubmit.setOnClickListener {
            val rating = when (binding.rgRating.checkedRadioButtonId) {
                binding.rb1.id -> 1
                binding.rb2.id -> 2
                binding.rb3.id -> 3
                binding.rb4.id -> 4
                binding.rb5.id -> 5
                else -> 0
            }
            val comment = binding.etComment.text.toString().trim()

            // 組合完整的的回饋內容
            val feedbackContent = "評分: $rating 星, 留言: $comment"

            // 檢查留言是否為空
            if (comment.isEmpty() && rating == 0) {
                Toast.makeText(this, "請選擇評分或填寫留言", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. 取得目前的時間字串
            val submissionDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            // 2. 呼叫 addFeedback 方法將回饋存入資料庫
            db.addFeedback(currentUsername, feedbackContent, submissionDate)

            // 3. 提示使用者成功訊息
            Toast.makeText(this, "感謝您的回饋！", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnBackToMenu.setOnClickListener {
            // 直接結束當前頁面，即可返回上一頁 (也就是主選單)
            finish()
        }
    }
}
