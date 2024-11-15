package com.example.umc_week4_didi

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mStartBtn: Button
    private lateinit var mStopBtn: Button
    private lateinit var mRecordBtn: Button
    private lateinit var mPauseBtn: Button
    private lateinit var mTimeTextView: TextView
    private lateinit var mRecordTextView: TextView
    private var timeThread: Thread? = null
    private var isRunning = false // 초기값 false로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = Color.parseColor("#4ea1d3")
        }

        // 버튼과 텍스트 뷰 초기화
        mStartBtn = findViewById(R.id.btn_start)
        mStopBtn = findViewById(R.id.btn_stop)
        mRecordBtn = findViewById(R.id.btn_record)
        mPauseBtn = findViewById(R.id.btn_pause)
        mTimeTextView = findViewById(R.id.timeView)
        mRecordTextView = findViewById(R.id.recordView)

        // Start 버튼 클릭 리스너
        mStartBtn.setOnClickListener {
            if (timeThread == null || !timeThread!!.isAlive) {
                it.visibility = View.GONE
                mStopBtn.visibility = View.VISIBLE
                mRecordBtn.visibility = View.VISIBLE
                mPauseBtn.visibility = View.VISIBLE

                isRunning = true
                timeThread = Thread(TimeThread())
                timeThread!!.start()
            }
        }

        // Stop 버튼 클릭 리스너
        mStopBtn.setOnClickListener {
            it.visibility = View.GONE
            mRecordBtn.visibility = View.GONE
            mStartBtn.visibility = View.VISIBLE
            mPauseBtn.visibility = View.GONE
            mRecordTextView.text = ""
            mTimeTextView.text = "00:00:00:00"

            // 스레드 중지
            timeThread?.interrupt()
            timeThread = null
            isRunning = false
        }

        // Record 버튼 클릭 리스너
        mRecordBtn.setOnClickListener {
            mRecordTextView.text = "${mRecordTextView.text}${mTimeTextView.text}\n"
        }

        // Pause 버튼 클릭 리스너
        mPauseBtn.setOnClickListener {
            isRunning = !isRunning
            mPauseBtn.text = if (isRunning) "일시정지" else "시작"
        }
    }

    // Handler를 통해 UI 업데이트
    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val mSec = msg.arg1 % 100
            val sec = (msg.arg1 / 100) % 60
            val min = (msg.arg1 / 100) / 60
            val hour = (msg.arg1 / 100) / 360

            @SuppressLint("DefaultLocale")
            val result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec)
            if (result == "00:01:15:00") {
                Toast.makeText(this@MainActivity, "1분 15초가 지났습니다.", Toast.LENGTH_SHORT).show()
            }
            mTimeTextView.text = result
        }
    }

    // 시간 증가를 관리하는 스레드
    inner class TimeThread : Runnable {
        override fun run() {
            var i = 0
            while (true) {
                if (Thread.interrupted()) break

                while (isRunning) { // 일시 정지 상태가 아닐 때만 실행
                    val msg = Message()
                    msg.arg1 = i++
                    handler.sendMessage(msg)

                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        return // 인터럽트 시 스레드 종료
                    }
                }
            }
        }
    }
}
