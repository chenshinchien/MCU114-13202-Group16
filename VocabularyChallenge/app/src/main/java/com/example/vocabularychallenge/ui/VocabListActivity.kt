package com.example.vocabularychallenge.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularychallenge.adapter.VocabAdapter
import com.example.vocabularychallenge.databinding.ActivityVocabListBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.model.Vocab

class VocabListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVocabListBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVocabListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        val reviewWrongIds = intent.getIntegerArrayListExtra("REVIEW_WRONG_IDS")

        if (reviewWrongIds != null && reviewWrongIds.isNotEmpty()) {
            // --- 情況 A：進入「測驗解析」模式 ---
            handleReviewMode(reviewWrongIds)
        } else {
            // --- 情況 B：進入「正常瀏覽」模式 ---
            handleNormalMode()
        }

        // 返回按鈕的邏輯在兩種模式下都一樣
        binding.btnBackToMenu.setOnClickListener {
            finish()
        }
    }

    /**
     * 處理「錯題解析」模式的邏輯
     */
    private fun handleReviewMode(wrongIds: List<Int>) {
        // a. 隱藏下拉式選單，因為我們不需要它
        binding.spinnerUnits.visibility = View.INVISIBLE

        // b. 更改頁面標題，讓使用者知道現在的模式
        //    (您可能需要在 XML 中加入一個 TextView 來顯示標題)
        //    假設您有一個 tvTitle TextView
        binding.tvTitle.text = "本次測驗錯題解析" // 如果沒有 tvTitle，可以忽略這行

        // c. 從資料庫中只獲取這些特定 ID 的單字
        val list = db.getWordsByIds(wrongIds)

        android.util.Log.d("ReviewFlow", "--- 3. VocabListActivity ---")
        android.util.Log.d("ReviewFlow", "進入解析模式，收到的錯題 ID 數量: ${wrongIds.size}")
        android.util.Log.d("ReviewFlow", "錯題 ID 列表: $wrongIds")
        android.util.Log.d("ReviewFlow", "從資料庫查詢到的單字數量: ${list.size}")
        if (list.isNotEmpty()) {
            android.util.Log.d("ReviewFlow", "查到的單字: ${list.map { it.english }}")
        }
        android.util.Log.d("ReviewFlow", "--------------------------") // 分隔線，方便查看

        // d. 設定 RecyclerView 來顯示解析的單字
        binding.recyclerVocab.layoutManager = LinearLayoutManager(this)
        binding.recyclerVocab.adapter = VocabAdapter(list)
    }

    /**
     * 處理「正常瀏覽」模式的邏輯
     */
    private fun handleNormalMode() {
        // a. 確保下拉式選單是可見的
        binding.spinnerUnits.visibility = View.VISIBLE
        binding.tvTitle.text = "單字列表" // 設定正常模式的標題

        // b. 照常設定下拉式選單的功能
        val units = arrayOf("全部", "單元 1", "單元 2") // 您可以根據需要擴充
        binding.spinnerUnits.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        binding.spinnerUnits.setSelection(0) // 預設選中 "全部"

        binding.spinnerUnits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val unit = position // 直接使用 position 作為 unit (0=全部, 1=單元1, ...)
                // 從資料庫取得對應單元的單字列表
                val list = db.getVocabList(unit)
                // 設定 RecyclerView
                binding.recyclerVocab.layoutManager = LinearLayoutManager(this@VocabListActivity)
                binding.recyclerVocab.adapter = VocabAdapter(list)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 不需要處理
            }
        }
    }
}
