package com.haeun.umc_week3

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // 표시할 탭 수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlbumTracksFragment()  // 첫 번째 탭에 표시할 Fragment
            1 -> AlbumDetailFragment()  // 두 번째 탭에 표시할 Fragment
            2 -> AlbumVideoFragment()   // 세 번째 탭에 표시할 Fragment
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
