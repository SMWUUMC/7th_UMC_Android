package com.example.umc_week2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // 현재 선택된 메뉴의 인덱스 저장
    private var currentSelectedIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 첫 화면으로 홈 프래그먼트 표시
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // BottomNavigationView 메뉴 선택 리스너 설정
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            // 각 메뉴에 해당하는 프래그먼트를 리스트로 정의
            val fragments =
                listOf(HomeFragment(), WriteFragment(), CalendarFragment(), UserFragment())

            // 선택된 메뉴에 따라 인덱스 결정
            val selectedIndex = when (item.itemId) {
                R.id.home -> 0
                R.id.write -> 1
                R.id.calendar -> 2
                R.id.user -> 3
                else -> return@setOnItemSelectedListener false
            }

            // 같은 탭을 누르면 동작 X
            if (selectedIndex != currentSelectedIndex) {
                // 애니메이션 설정
                val (inAnim, outAnim) = if (selectedIndex > currentSelectedIndex) {
                    R.anim.slide_in_right to R.anim.slide_out_left
                } else {
                    R.anim.slide_in_left to R.anim.slide_out_right
                }

                // 애니메이션 적용
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(inAnim, outAnim)
                    .replace(R.id.fragment_container, fragments[selectedIndex])
                    .commit()
                currentSelectedIndex = selectedIndex // 인덱스 업데이트
            }
            true
        }
    }
}
