package com.example.umc_week3.ui.main.album

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc_week3.R
import com.example.umc_week3.data.entities.Album
import com.example.umc_week3.data.entities.Like
import com.example.umc_week3.data.local.SongDatabase
import com.example.umc_week3.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private var albumId: Int = -1 // 전달받은 앨범 ID
    private lateinit var songDatabase: SongDatabase
    private var album: Album? = null // 앨범 객체 선언
    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE)
    }


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

        // 좋아요 버튼 클릭 이벤트
        binding.albumLikeIv.setOnClickListener {
            album?.let {
                toggleAlbumLike(it)
            }
        }


    }

    private fun fetchAlbumData() {
        lifecycleScope.launch {
            val albumData = songDatabase.albumDao().getAlbumById(albumId)

            if (albumData != null) {
                // 로드된 앨범 데이터를 album 변수에 저장
                album = albumData

                // UI 업데이트
                binding.albumTitle.text = albumData.title
                binding.albumArtist.text = albumData.singer
                binding.albumInfo.text = "${albumData.releaseDate} | ${albumData.type} | ${albumData.genre}"
                albumData.coverImg?.let { binding.albumCover.setImageResource(it) }

                // 좋아요 상태 표시
                val userId = getCurrentUserId()
                if (songDatabase.likeDao().isAlbumLikedByUser(userId, albumId)) {
                    binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                } else {
                    binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                }

                // ViewPager와 TabLayout 설정
                setupViewPager(albumId)
            } else {
                Log.e("AlbumFragment", "Album not found for id: $albumId")
            }
        }
    }

    private fun toggleAlbumLike(album: Album) {
        val userId = getCurrentUserId()
        lifecycleScope.launch {
            val isLiked = songDatabase.likeDao().isAlbumLikedByUser(userId, album.id)

            if (isLiked) {
                // 좋아요 해제
                songDatabase.likeDao().deleteLike(userId, album.id)
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                Toast.makeText(requireContext(), "좋아요 해제", Toast.LENGTH_SHORT).show()
            } else {
                // 좋아요 추가
                val like = Like(userId = userId, albumId = album.id)
                songDatabase.likeDao().insertLike(like)
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                Toast.makeText(requireContext(), "좋아요 추가", Toast.LENGTH_SHORT).show()
            }

            // 좋아요 상태 확인용 로그
            val likes = songDatabase.likeDao().getLikesByUser(userId)
            likes.forEach { Log.d("LikeDebug", "User $userId liked album ${it.albumId}") }
        }
    }

    private fun getCurrentUserId(): Long {
        return sharedPreferences.getLong("userId", -1L)
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