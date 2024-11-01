package com.example.umc_week4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var time = 0L
    private var isRunning = false

    private lateinit var stopwatchThread: Thread
    private var isThreadRunning = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // start/pause 버튼
        binding.startButton.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                binding.startButton.text = "Pause"
            } else {
                binding.startButton.text = "Start"
            }
        }

        // clear 버튼
        binding.clearButton.setOnClickListener {
            time = 0L
            updateTimeDisplay()
        }

        // 스톱워치 스레드 초기화
        stopwatchThread = Thread {
            while (isThreadRunning) {
                if (isRunning) {
                    time += 10
                    runOnUiThread { updateTimeDisplay() }
                    Thread.sleep(10)
                }
            }
        }

        // 스레드 시작 (최초 한 번만 실행)
        if (!isThreadRunning) {
            isThreadRunning = true
            stopwatchThread.start()
        }
    }

    // 시간 업데이트
    private fun updateTimeDisplay() {
        val minutes = (time / 1000) / 60
        val seconds = (time / 1000) % 60
        val milliseconds = (time % 1000) / 10
        binding.timeTextView.text = String.format("%02d:%02d.%02d", minutes, seconds, milliseconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        isThreadRunning = false
        stopwatchThread.interrupt()
    }
}
