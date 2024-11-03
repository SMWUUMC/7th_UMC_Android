package com.example.umc_week3_didi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_week3_didi.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private var albumTitle: String? = null
    private var artistName: String? = null
    private var albumInfo: String? = null
    private var albumCoverResId: Int? = null
    private var trackList: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 데이터를 받음
        albumTitle = arguments?.getString("albumTitle")
        artistName = arguments?.getString("artistName")
        albumInfo = arguments?.getString("albumInfo")
        albumCoverResId = arguments?.getInt("albumCoverResId")
        trackList = arguments?.getStringArrayList("trackList")

        // 로그로 trackList 값 확인
        Log.d("AlbumFragment", "TrackList: $trackList")

        // 받은 데이터를 UI에 반영
        binding.albumTitle.text = albumTitle
        binding.albumArtist.text = artistName
        binding.albumInfo.text = albumInfo
        albumCoverResId?.let { binding.albumCover.setImageResource(it) }


        val adapter = AlbumPagerAdapter(
            this,
            trackList ?: emptyList(),
            artistName ?: ""
        )
        binding.viewPager.adapter = adapter


        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "영상"
            }
        }.attach()

        // 뒤로가기 버튼 클릭 시 HomeFragment로 이동
        binding.backIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
