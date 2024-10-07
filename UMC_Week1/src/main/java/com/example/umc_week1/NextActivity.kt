package com.example.umc_week1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week1.databinding.ActivityNextBinding

class NextActivity : AppCompatActivity() {

    // 아이콘 리소스 ID를 전달받기 위한 키
    companion object {
        const val ICON_ID = "icon_id"
    }

    private lateinit var binding: ActivityNextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 전달된 아이콘 리소스 ID 받기
        val iconId = intent.getIntExtra(ICON_ID, 0)

        // 아이콘을 설정
        if (iconId != 0) {
            binding.iconImage.setImageResource(iconId)
        }
    }
}