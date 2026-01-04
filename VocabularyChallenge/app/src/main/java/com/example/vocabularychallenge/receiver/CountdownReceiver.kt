package com.example.vocabularychallenge.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CountdownReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent?.getStringExtra("msg") ?: "Time's up"
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
