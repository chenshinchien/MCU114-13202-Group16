package com.example.vocabularychallenge.ui

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularychallenge.databinding.ActivityRecordsBinding
import com.example.vocabularychallenge.db.DatabaseHelper
import com.example.vocabularychallenge.adapter.RecordAdapter
import com.example.vocabularychallenge.model.UserRecord

class RecordsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        val username = intent.getStringExtra("username") ?: return
        val cursor: Cursor = db.getRecordsForUser(username)
        val list = mutableListOf<UserRecord>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(UserRecord(
                    it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_DATE)),
                    it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_UNITREC)),
                    it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_SCORE)),
                    it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_TIME))
                ))
            }
        }
        binding.recyclerRecords.layoutManager = LinearLayoutManager(this)
        binding.recyclerRecords.adapter = RecordAdapter(list)

        binding.btnBackToMenu.setOnClickListener {
            finish()
        }

    }

}
