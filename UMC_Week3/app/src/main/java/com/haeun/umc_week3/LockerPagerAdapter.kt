package com.haeun.umc_week3
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LockerSavedSongFragment()
            1 -> LockerMusicFilesFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
