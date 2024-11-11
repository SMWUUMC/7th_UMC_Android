package com.example.umc_week3

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.umc_week3.R

object MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private var updateSeekBarThread: Thread? = null
    private val handler = Handler(Looper.getMainLooper())
    private var seekBarCallback: ((Int) -> Unit)? = null
    private var completionListener: (() -> Unit)? = null
    private var isRepeat = false // 반복 재생 여부

    // MediaPlayer 초기화
    fun initPlayer(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.dumb_dumb).apply {
                setOnPreparedListener {
                    isPrepared = true
                }
                setOnCompletionListener {
                    handler.post {
                        completionListener?.invoke()

                        // 반복 재생이 활성화된 경우에만 반복 재생 시작
                        if (isRepeat) {
                            seekTo(0)
                            play()
                        } else {
                            stopSeekBarUpdate()
                        }
                    }
                }
            }
        }
    }

    // 음악 재생
    fun play() {
        if (isPrepared && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            startSeekBarUpdate()
        }
    }

    // 음악 일시정지
    fun pause() {
        mediaPlayer?.pause()
        stopSeekBarUpdate()
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
        if (updateSeekBarThread?.isAlive == true) return

        updateSeekBarThread = Thread {
            try {
                while (mediaPlayer?.isPlaying == true) {
                    val currentPosition = getCurrentPosition()
                    handler.post { seekBarCallback?.invoke(currentPosition) }
                    Thread.sleep(1000)
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

    // 외부에서 호출 가능한 setOnCompletionListener 추가
    fun setOnCompletionListener(listener: () -> Unit) {
        completionListener = listener
    }

    // 반복 재생 설정
    fun setLooping(loop: Boolean) {
        isRepeat = loop // 내부적으로 반복 재생 플래그를 업데이트
        mediaPlayer?.isLooping = loop
    }

    // MediaPlayer 해제
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
        stopSeekBarUpdate()
    }
}
