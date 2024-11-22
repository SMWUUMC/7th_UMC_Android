package com.haeun.umc_week3

import SavedSongFragment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.d("LockerVPAdapter", "createFragment position: $position") // 로그 추가
        return when (position) {
            0 -> SavedSongFragment() // 첫 번째 탭에 보여줄 프래그먼트
            1 -> MusicFileFragment()  // 두 번째 탭에 보여줄 프래그먼트
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
