package com.example.umc_week1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_week1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // ViewBinding 변수 선언
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 각 아이콘에 클릭 리스너 설정
        binding.happy.setOnClickListener {
            navigateToNextActivity(R.drawable.yellow) }

        binding.excited.setOnClickListener {
            navigateToNextActivity(R.drawable.blue) }

        binding.ordinary.setOnClickListener {
            navigateToNextActivity(R.drawable.violet) }

        binding.anxiety.setOnClickListener {
            navigateToNextActivity(R.drawable.green) }

        binding.angry.setOnClickListener {
            navigateToNextActivity(R.drawable.red) }
    }

    // 화면 전환 메서드
    private fun navigateToNextActivity(iconId: Int) {
        val intent = Intent(this, NextActivity::class.java)
        intent.putExtra(NextActivity.ICON_ID, iconId)
        startActivity(intent)
    }

}