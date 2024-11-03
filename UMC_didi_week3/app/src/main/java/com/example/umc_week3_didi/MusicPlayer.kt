package com.example.umc_week3_didi

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log

object MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private var isPlaying = false
    private var updateSeekBarThread: Thread? = null
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var seekBarCallback: ((Int) -> Unit)? = null

    // MediaPlayer 초기화
    fun initPlayer(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.dumb_dumb).apply {
                setOnPreparedListener {
                    isPrepared = true
                }
                setOnCompletionListener {
                    setPlaying(false) // 강제로 상태를 갱신
                    handler?.post {
                        seekBarCallback?.invoke(getCurrentPosition()) // 현재 위치로 UI 업데이트
                    }
                }
            }
        }
    }



    // 음악 재생
    fun play() {
        if (isPrepared && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            isPlaying = true
            startSeekBarUpdate()
        }
    }

    // 음악 일시정지
    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            stopSeekBarUpdate()
        }
    }

    // 재생 상태 반환
    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun setPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }


    // 현재 재생 위치 반환
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    // 전체 재생 길이 반환
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    // 재생 위치 설정
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    // SeekBar 업데이트 - Thread 시작
    fun startSeekBarUpdate() {
        updateSeekBarThread = Thread {
            try {
                while (isPlaying) {
                    val currentPosition = getCurrentPosition()

                    // UI 업데이트
                    handler?.post {
                        seekBarCallback?.invoke(currentPosition)
                    }

                    Thread.sleep(1000) // 1초마다 업데이트
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        updateSeekBarThread?.start()
    }

    // updateSeekBarThread 중지
    fun stopSeekBarUpdate() {
        updateSeekBarThread?.interrupt()
        updateSeekBarThread = null
    }

    // SeekBar의 위치를 업데이트하는 콜백 설정
    fun setSeekBarCallback(callback: (Int) -> Unit) {
        seekBarCallback = callback
    }

    // MediaPlayer 해제
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
        isPlaying = false
        stopSeekBarUpdate()
    }
}
