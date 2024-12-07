package com.example.umc_week3.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_week3.ui.main.album.bside.BsideFragment
import com.example.umc_week3.ui.main.album.detail.DetailFragment
import com.example.umc_week3.ui.main.album.video.VideoFragment

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