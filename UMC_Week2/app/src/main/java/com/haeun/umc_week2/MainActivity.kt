package com.haeun.umc_week2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navi)

        // 최초 로드할 프래그먼트 설정
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "fade") // 처음 실행 시 HomeFragment 로드
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            var selectedFragment: Fragment? = null

            when (menuItem.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_edit -> selectedFragment = EditFragment()
                R.id.navigation_calendar -> selectedFragment = CalendarFragment()
                R.id.navigation_profile -> selectedFragment = ProfileFragment()
            }


            // 선택된 프래그먼트가 EditFragment인 경우 애니메이션 적용하여 로드
            if (selectedFragment is EditFragment) {
                loadFragment(selectedFragment, "fade")
            } else if (selectedFragment != null) {
                loadFragment(selectedFragment, "fade")
            }

            true
        }

    }


    // 프래그먼트 전환 함수
    private fun loadFragment(fragment: Fragment, animationType: String) {
        // 같은 프래그먼트가 아닌 경우에만 로드
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment?.javaClass != fragment.javaClass) {
            val transaction = supportFragmentManager.beginTransaction()

            // 페이드 애니메이션 적용
            transaction.setCustomAnimations(
                android.R.anim.fade_in,  // 들어오는 애니메이션
                android.R.anim.fade_out, // 나가는 애니메이션
                android.R.anim.fade_in,  // 뒤로가기 시 들어오는 애니메이션
                android.R.anim.fade_out  // 뒤로가기 시 나가는 애니메이션
            )

            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}


