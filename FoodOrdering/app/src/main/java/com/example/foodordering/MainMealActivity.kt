package com.example.foodordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class MainMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_meal)

        val radioGroup = findViewById<RadioGroup>(R.id.radioMain)
        val btnFinish = findViewById<Button>(R.id.btnDone1)

        btnFinish.setOnClickListener {

            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedButton = findViewById<RadioButton>(selectedId)
                val selectedMeal = selectedButton.text.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("selection", selectedMeal)

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                android.widget.Toast.makeText(this, "請先選擇主餐！", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
