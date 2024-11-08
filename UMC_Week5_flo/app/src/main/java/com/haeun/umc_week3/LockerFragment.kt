package com.haeun.umc_week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.haeun.umc_week3.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locker, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.locker_content_vp)
        val tabLayout = view.findViewById<TabLayout>(R.id.locker_content_tb)

        val adapter = LockerPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악 파일"
                else -> null
            }
        }.attach()

        return view
    }

}