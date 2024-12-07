package com.example.umc_week3.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.umc_week3.R

object MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private val handler = Handler(Looper.getMainLooper())
    private var seekBarCallback: ((Int) -> Unit)? = null
    private var completionListener: (() -> Unit)? = null
    private var isRepeat = false // 반복 재생 여부
    private var currentSongId: Int = -1

    val songs = arrayOf(
        R.raw.dumb_dumb,
        R.raw.huff_n_puff,
        R.raw.campfire,
        R.raw.pink_venom,
        R.raw.ready_for_love,
        R.raw.young_dumb_stupid,
        R.raw.love_me_like_this,
        R.raw.paxxword
    )

    // MediaPlayer 초기화
    fun initPlayer(context: Context, songId: Int) {
        release() // 기존 MediaPlayer 해제

        try {
            // 배열에서 리소스를 가져옴
            val resourceId = songs.getOrNull(songId-1)
            if (resourceId == null) {
                Log.e("MusicPlayer", "Invalid songId: $songId")
                return
            }

            mediaPlayer = MediaPlayer.create(context, resourceId)
            if (mediaPlayer == null) {
                Log.e("MusicPlayer", "MediaPlayer initialization failed. Resource ID: $resourceId")
                return
            }

            mediaPlayer?.apply {
                setOnPreparedListener {
                    isPrepared = true
                    Log.d("MusicPlayer", "MediaPlayer prepared and starting playback.")
                    start()
                }
                setOnCompletionListener {
                    if (isRepeat) {
                        seekTo(0)
                        start()
                    } else {
                        completionListener?.invoke()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MusicPlayer", "Error initializing MediaPlayer for songId: $songId", e)
        }
    }

    fun getCurrentSongId(): Int {
        return currentSongId
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
        if (!isPrepared) return // 준비가 안 된 경우 업데이트 중단

        handler.post(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        seekBarCallback?.invoke(it.currentPosition)
                        handler.postDelayed(this, 1000) // 1초마다 업데이트
                    }
                }
            }
        })
    }

    // updateSeekBarThread 중지
    fun stopSeekBarUpdate() {
        handler.removeCallbacksAndMessages(null)
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
        isRepeat = loop
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