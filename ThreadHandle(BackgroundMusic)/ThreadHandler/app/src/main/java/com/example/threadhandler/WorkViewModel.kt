package com.example.threadhandler

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel


class WorkViewModel(application: Application) : AndroidViewModel(application) {

    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile private var running = false
    private var player: MediaPlayer? = null


    fun start() {
        if (running) return
        running = true
        _status.postValue("Preparing…")
        _progress.postValue(0)

        player = MediaPlayer.create(getApplication(), R.raw.background_music)
        player?.isLooping = true
        player?.start()

        worker.post {
            try {
                Thread.sleep(10000) // discovery，準備工作放這裏
                _status.postValue("Working…")
                for (i in 1..100) {
                    if (!running) break
                    Thread.sleep(3500) // 真正的背㬌工作放這裏
                    _progress.postValue(i)
                }
                _status.postValue(if (running) "背景工作結朿！" else "Canceled")
                stopMusic()
                running = false
            } catch (_: InterruptedException) {
                _status.postValue("Canceled")
                stopMusic()
                running = false
            }
        }
    }

    private fun stopMusic() {
        player?.stop()
        player?.release()
        player = null
    }

    fun cancel() {
        running = false
        stopMusic()
    }

    override fun onCleared() {
        running = false
        stopMusic()
        handlerThread.quitSafely()
        super.onCleared()
    }
}