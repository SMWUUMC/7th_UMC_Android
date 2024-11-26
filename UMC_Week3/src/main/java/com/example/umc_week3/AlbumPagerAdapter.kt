package com.example.umc_week3

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(
    fragment: Fragment,
    private val albumIdx: Int // albumIdx를 전달받음
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BsideFragment.newInstance(albumIdx) // albumIdx를 기반으로 BsideFragment 생성
            1 -> DetailFragment()
            2 -> VideoFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}