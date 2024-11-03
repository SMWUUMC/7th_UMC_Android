package com.example.umc_week3_didi

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(
    fragment: Fragment,
    private val trackList: List<String>,
    private val artistName: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BsideFragment.newInstance(trackList, artistName)
            1 -> DetailFragment()
            2 -> VideoFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

