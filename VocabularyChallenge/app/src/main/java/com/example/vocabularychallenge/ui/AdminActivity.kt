package com.example.vocabularychallenge.ui

import android.content.Intent // 【新增】引入 Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularychallenge.ui.AdminRecordAdapter // 【修正】引入正確的 Adapter
import com.example.vocabularychallenge.ui.FeedbackAdapter // 【修正】引入正確的 Adapter
import com.example.vocabularychallenge.databinding.ActivityAdminBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.model.TestResult

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var db: DatabaseHelper
    private lateinit var recordAdapter: AdminRecordAdapter
    private lateinit var feedbackAdapter: FeedbackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // 設定兩個 RecyclerView
        setupTestRecordsRecyclerView()
        loadTestRecords()

        setupFeedbackRecyclerView()
        loadFeedbackRecords()

        // 設定「返回登入頁」按鈕的點擊事件
        binding.btnBackToLogin.setOnClickListener {
            // 建立一個 Intent 來啟動 LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            // 設定 Flag 來清除目前 Activity 堆疊中的所有 Activity，並啟動一個新的 Task
            // 這可以防止使用者從登入頁按下返回鍵時又回到管理者頁面
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setupTestRecordsRecyclerView() {
        recordAdapter = AdminRecordAdapter(emptyList())
        binding.rvTestRecords.apply {
            adapter = recordAdapter
            layoutManager = LinearLayoutManager(this@AdminActivity)
        }
    }

    private fun loadTestRecords() {
        val allRecords: List<TestResult> = db.getAllResults()
        recordAdapter.updateData(allRecords)
    }

    private fun setupFeedbackRecyclerView() {
        feedbackAdapter = FeedbackAdapter(emptyList())
        binding.rvFeedbackRecords.apply {
            adapter = feedbackAdapter
            layoutManager = LinearLayoutManager(this@AdminActivity)
        }
    }

    private fun loadFeedbackRecords() {
        val allFeedback = db.getAllFeedback()
        feedbackAdapter.updateData(allFeedback) // 【修正】呼叫 updateData
    }
}
