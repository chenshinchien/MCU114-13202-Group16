package com.example.vocabularychallenge.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularychallenge.databinding.ActivityLoginBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.service.UsageService

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        binding.btnLogin.setOnClickListener {
            val user = binding.etUsername.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            // 檢查帳號密碼是否為空
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "請輸入帳號與密碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 【新增】: 優先判斷是否為管理員帳號
            // 假設管理員帳號是 "admin"，密碼也是 "admin"
            if (user == "admin" && pass == "admin") {
                Toast.makeText(this, "管理員登入成功", Toast.LENGTH_SHORT).show()
                // 啟動管理員頁面 (AdminActivity)
                val adminIntent = Intent(this, AdminActivity::class.java)
                startActivity(adminIntent)
                finish() // 關閉登入頁面
                return@setOnClickListener // 結束後續的程式碼執行
            }

            // 【保留】: 如果不是管理員，則執行原本的一般使用者驗證
            if (db.validateUser(user, pass)) {
                Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()

                // 啟動 Background Service 並帶 username
                val serviceIntent = Intent(this, UsageService::class.java).apply {
                    putExtra("username", user)
                }
                startService(serviceIntent)

                // 啟動主選單頁面 (MainMenuActivity)
                val mainIntent = Intent(this, MainMenuActivity::class.java)
                mainIntent.putExtra("username", user)
                startActivity(mainIntent)
                finish()
            } else {
                // 如果兩種驗證都失敗，提示錯誤
                Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
