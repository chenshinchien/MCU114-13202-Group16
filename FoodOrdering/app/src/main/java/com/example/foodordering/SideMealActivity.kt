package com.example.foodordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SideMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_meal)

        val cbFries = findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = findViewById<CheckBox>(R.id.cbSalad)
        val cbCorn = findViewById<CheckBox>(R.id.cbCorn)

        val btnFinish = findViewById<Button>(R.id.btnDone2)

        btnFinish.setOnClickListener {
            val selectedSides = mutableListOf<String>()

            if (cbFries.isChecked) selectedSides.add(cbFries.text.toString())
            if (cbSalad.isChecked) selectedSides.add(cbSalad.text.toString())
            if (cbCorn.isChecked) selectedSides.add(cbCorn.text.toString())

            if (selectedSides.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("selection", selectedSides.joinToString(", "))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "請至少選擇一項副餐！", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

