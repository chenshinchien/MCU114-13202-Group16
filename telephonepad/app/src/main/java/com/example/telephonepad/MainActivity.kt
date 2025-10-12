package com.example.telephonepad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var tvNumber: TextView? = null
    private var btnCall: Button? = null
    private var btnClear: Button? = null
    private val btnIds = intArrayOf(
        R.id.btn0, R.id.btn1, R.id.btn2,
        R.id.btn3, R.id.btn4, R.id.btn5,
        R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNumber = findViewById<TextView?>(R.id.tvNumber)
        btnCall = findViewById<Button?>(R.id.btnCall)
        btnClear = findViewById<Button?>(R.id.btnClear)

        // 設定數字按鈕的 Listener
        for (id in btnIds) {
            val btn = findViewById<Button?>(id)
            btn.setOnClickListener(this)
        }

        // 清除按鈕
        btnClear!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                tvNumber!!.setText("電話號碼：")
            }
        })

        // CALL 按鈕
        btnCall!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val num = tvNumber!!.getText().toString().replace("電話號碼：", "")
                if (!num.isEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num))
                    startActivity(intent)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        val btn = v as Button
        val current = tvNumber!!.getText().toString()
        val newNum = current.replace("電話號碼：", "") + btn.getText().toString()
        tvNumber!!.setText("電話號碼：" + newNum)
    }
}