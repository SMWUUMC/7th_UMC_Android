package com.example.umc_week3.ui.main.storage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_week3.ui.main.storage.file.MusicFileFragment
import com.example.umc_week3.ui.main.storage.song.SavedSongFragment
import com.example.umc_week3.ui.main.storage.album.SavedAlbumFragment

class StoragePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            2 -> SavedAlbumFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}