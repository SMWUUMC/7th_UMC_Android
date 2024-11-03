package com.haeun.umc_week4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haeun.umc_week4.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timeMillis: Long = 0L
    private var isRunning = false
    private var timerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener { startOrPauseTimer() }
        binding.clearButton.setOnClickListener { clearTimer() }
    }

    private fun startOrPauseTimer() { // Start, Pause 상태 전환
        if (isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() { // 코루틴을 사용하여 타이머 시작, 10밀리초마다 timeMillis 업데이트
        isRunning = true
        binding.startButton.text = "Pause"

        timerJob = lifecycleScope.launch {
            while (isActive) {
                delay(10L)
                timeMillis += 10L
                updateTimerText()
            }
        }
    }

    private fun pauseTimer() { // 타이머 일시정지, timerJob 취소
        isRunning = false
        binding.startButton.text = "Start"
        timerJob?.cancel()
    }

    private fun clearTimer() {
        // 타이머가 실행 중 : Clear 버튼을 누르면 멈추지 않고 0초로 초기화
        // 타이머가 일시정지 상태 : Clear 버튼을 누르면 0초로 초기화, 화면에 표시
        if (isRunning) {
            timeMillis = 0L
            updateTimerText()
        } else {
            timeMillis = 0L
            updateTimerText()
        }
    }

    private fun updateTimerText() {
        val seconds = (timeMillis / 1000) % 60
        val minutes = (timeMillis / 60000) % 60
        val milliseconds = (timeMillis % 1000) / 10
        binding.timerTextView.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }
}
