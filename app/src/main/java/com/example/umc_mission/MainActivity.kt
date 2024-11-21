package com.example.umc_mission

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_mission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
    }

    // 하단 바와 프래그먼트 설정
    private fun initBottomNavigation(){

        // 초기 화면 설정 (홈화면으로)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, HomeFragment())
            .commitAllowingStateLoss()

        // 하단 바 메뉴 아이콘 클릭 시 동작
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                // 홈 아이콘 선택
                R.id.navi_home -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.main_container, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                // 작성 아이콘 선택
                R.id.navi_writing -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.main_container, WritingFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                // 일정 아이콘 선택
                R.id.navi_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.main_container, CalendarFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                // 마이페이지 아이콘 선택
                R.id.navi_mypage -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.main_container, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }
}