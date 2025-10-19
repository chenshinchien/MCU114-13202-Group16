package com.example.foodordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val radioGroup = findViewById<RadioGroup>(R.id.radioDrink)
        val btnFinish = findViewById<Button>(R.id.btnDone3)

        btnFinish.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedButton = findViewById<RadioButton>(selectedId)
                val selectedDrink = selectedButton.text.toString()


                val resultIntent = Intent()
                resultIntent.putExtra("selection", selectedDrink)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "請先選擇飲料！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
