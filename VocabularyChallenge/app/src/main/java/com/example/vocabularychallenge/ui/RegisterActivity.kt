package com.example.vocabularychallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.vocabularychallenge.databinding.ActivityRegisterBinding
import com.example.vocabularychallenge.db.DatabaseHelper

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            val user = binding.etUsername.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "請輸入帳號密碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val ok = db.addUser(user, pass)
            if (ok) {
                Toast.makeText(this, "註冊成功，請回登入", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "註冊失敗（帳號可能重複）", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
