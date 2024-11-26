package com.example.umc_week3

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StoragePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}