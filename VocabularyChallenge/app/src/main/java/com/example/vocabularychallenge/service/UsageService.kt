package com.example.vocabularychallenge.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import com.example.vocabularychallenge.db.DatabaseHelper
import kotlinx.coroutines.*

class UsageService: Service() {
    private var startTime = 0L
    private var username: String? = null
    private lateinit var db: DatabaseHelper
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        db = DatabaseHelper(this)
        startTime = SystemClock.elapsedRealtime()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        username = intent?.getStringExtra("username")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        /*
         * START OF MODIFICATION
         *
         * The following code block was causing an incorrect test record with a score of 0
         * to be created whenever the service was destroyed.
         * The purpose of this service is likely to track total usage time, which should not
         * be recorded in the test results table.
         *
         * I am commenting out this block to fix the bug. If you want to log app usage time
         * in the future, you should create a dedicated table and a new function in DatabaseHelper
         * for this purpose.
         */
        /*
        val elapsed = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()

        val now = java.text.SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        username?.let {
            // This is the problematic line that has been disabled.
            db.recordResult(it, 0, 0, elapsed, now)
        }
        */
        // END OF MODIFICATION

        scope.cancel()
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
