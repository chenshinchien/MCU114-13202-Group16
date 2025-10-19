package com.example.foodordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textSelection: TextView

    private val REQUEST_MAIN_MEAL = 1
    private val REQUEST_SIDE_MEAL = 2
    private val REQUEST_DRINK = 3

    private var mainMeal: String? = null
    private var sideMeal: String? = null
    private var drink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textSelection = findViewById(R.id.textSelection)
        val btnMainMeal = findViewById<Button>(R.id.btnMainMeal)
        val btnSideMeal = findViewById<Button>(R.id.btnSideMeal)
        val btnDrink = findViewById<Button>(R.id.btnDrink)
        val btnOrder = findViewById<Button>(R.id.btnOrder)

        btnMainMeal.setOnClickListener {
            startActivityForResult(Intent(this, MainMealActivity::class.java), REQUEST_MAIN_MEAL)
        }

        btnSideMeal.setOnClickListener {
            startActivityForResult(Intent(this, SideMealActivity::class.java), REQUEST_SIDE_MEAL)
        }

        btnDrink.setOnClickListener {
            startActivityForResult(Intent(this, DrinkActivity::class.java), REQUEST_DRINK)
        }

        btnOrder.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java).apply {
                putExtra("mainMeal", mainMeal ?: "未選擇")
                putExtra("sideMeal", sideMeal ?: "未選擇")
                putExtra("drink", drink ?: "未選擇")
            }
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selection = data.getStringExtra("selection")

            when (requestCode) {
                REQUEST_MAIN_MEAL -> mainMeal = selection
                REQUEST_SIDE_MEAL -> sideMeal = selection
                REQUEST_DRINK -> drink = selection
            }

            textSelection.text = buildString {
                append("目前選擇：\n")
                append("主餐：${mainMeal ?: "未選擇"}\n")
                append("副餐：${sideMeal ?: "未選擇"}\n")
                append("飲料：${drink ?: "未選擇"}")
            }
        }

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val shouldReset = data?.getBooleanExtra("resetOrder", false) ?: false
            if (shouldReset) {
                mainMeal = null
                sideMeal = null
                drink = null
                textSelection.text = "目前選擇：\n主餐：未選擇\n副餐：未選擇\n飲料：未選擇"
            }
        }

    }
}

