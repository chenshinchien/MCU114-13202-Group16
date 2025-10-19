package com.example.foodordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirm)

        val tvResult = findViewById<TextView>(R.id.textView4)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)

        val mainMeal = intent.getStringExtra("mainMeal") ?: "未選擇"
        val sideMeal = intent.getStringExtra("sideMeal") ?: "未選擇"
        val drink = intent.getStringExtra("drink") ?: "未選擇"

        tvResult.text = "感謝您的點餐！\n主餐：$mainMeal\n副餐：$sideMeal\n飲料：$drink"

        btnConfirm.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("resetOrder", true)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

