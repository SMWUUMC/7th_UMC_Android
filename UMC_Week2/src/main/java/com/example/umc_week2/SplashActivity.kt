package com.example.umc_week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // 스플래시 화면 레이아웃 설정

        // 일정 시간 후 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java)) // MainActivity로 이동
            finish() // SplashActivity 종료
        }, 2000) // 2000ms = 2초 후에 MainActivity로 이동
    }
}
