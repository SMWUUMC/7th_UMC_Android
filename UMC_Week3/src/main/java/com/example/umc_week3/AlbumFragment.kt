package com.example.umc_week3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc_week3.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private var albumId: Int = -1 // 전달받은 앨범 ID
    private lateinit var songDatabase: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RoomDB 초기화
        songDatabase = SongDatabase.getDatabase(requireContext())

        // arguments에서 albumId를 받음
        albumId = arguments?.getInt("albumId") ?: -1

        if (albumId != -1) {
            fetchAlbumData()
        } else {
            Log.e("AlbumFragment", "Invalid albumId passed to fragment")
        }

        // 뒤로가기 버튼 클릭 시 이전 화면으로 이동
        binding.backIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun fetchAlbumData() {
        lifecycleScope.launch {
            val album = songDatabase.albumDao().getAlbumById(albumId)

            if (album != null) {
                // UI 업데이트
                binding.albumTitle.text = album.title
                binding.albumArtist.text = album.singer
                binding.albumInfo.text = "${album.releaseDate} | ${album.type} | ${album.genre}"
                album.coverImg?.let { binding.albumCover.setImageResource(it) }

                // ViewPager와 TabLayout 설정
                setupViewPager(albumId)
            } else {
                Log.e("AlbumFragment", "Album not found for id: $albumId")
            }
        }
    }

    private fun setupViewPager(albumId: Int) {
        val adapter = AlbumPagerAdapter(this, albumId) // albumId를 전달
        binding.viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "영상"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(albumId: Int): AlbumFragment {
            val fragment = AlbumFragment()
            val args = Bundle()
            args.putInt("albumId", albumId)
            fragment.arguments = args
            return fragment
        }
    }
}