/*
package com.example.vocabularychallenge.ui

import android.content.Context
import android.media.MediaPlayer
import com.example.vocabularychallenge.R
import android.util.Log

// 使用 object 關鍵字，它會自動變成一個單例 (Singleton)
// 這就是我們的中央音樂控制器
object MusicManager {

    private var mediaPlayer: MediaPlayer? = null

    // 播放主選單/非測驗頁面的音樂
    fun playMainMenuMusic(context: Context) {
        // 如果音樂還沒被建立或沒有在播放，就建立並播放它
        if (mediaPlayer == null || mediaPlayer?.isPlaying == false) {
            // 確保先釋放舊的資源，避免重複建立
            mediaPlayer?.release()

            try {
                mediaPlayer = MediaPlayer.create(context, R.raw.main_theme)
                mediaPlayer?.isLooping = true // 設定循環播放
                mediaPlayer?.start()
                Log.d("MusicManager", "主選單音樂已開始或重新開始播放。")
            } catch (e: Exception) {
                Log.e("MusicManager", "播放音樂失敗！請檢查 res/raw/ 目錄下是否有 main_theme.mp3", e)
            }
        } else {
            Log.d("MusicManager", "音樂已在播放中，不執行任何操作。")
        }
    }

    // 暫停音樂
    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            Log.d("MusicManager", "音樂已暫停。")
        }
    }

    // 停止並徹底釋放音樂資源 (App關閉或登出時使用)
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("MusicManager", "音樂已停止並釋放。")
    }
}
*/