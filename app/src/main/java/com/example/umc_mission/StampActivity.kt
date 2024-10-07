package com.example.umc_mission

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_mission.databinding.ActivityStampBinding

class StampActivity : AppCompatActivity() {

    // binding 변수 선언
    private lateinit var binding: ActivityStampBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 뷰 바인딩 초기화
        binding = ActivityStampBinding.inflate(layoutInflater)

        // 바인딩된 루트 뷰로 레이아웃 설정
        setContentView(binding.root)

        // 이미지버튼 클릭 이벤트(happy)
        binding.btnHappy.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
    }
}