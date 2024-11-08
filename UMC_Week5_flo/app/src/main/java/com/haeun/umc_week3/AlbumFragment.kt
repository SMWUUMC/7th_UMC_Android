package com.haeun.umc_week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.haeun.umc_week3.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , HomeFragment())
                .commitAllowingStateLoss()
        }

        // Adapter를 ViewPager에 설정
        val albumPagerAdapter = AlbumPagerAdapter(this)
        binding.albumContentVp.adapter = albumPagerAdapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡" // 첫 번째 탭 이름
                1 -> tab.text = "상세정보" // 두 번째 탭 이름
                2 -> tab.text = "영상" // 세 번째 탭 이름
            }
        }.attach()

        return binding.root
    }
}
