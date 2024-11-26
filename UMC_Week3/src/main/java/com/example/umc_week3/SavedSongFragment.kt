package com.example.umc_week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.databinding.FragmentSavedSongBinding
import kotlinx.coroutines.launch

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedSongBinding? = null
    private val binding get() = _binding!!

    private lateinit var songAdapter: SavedSongAdapter
    private lateinit var songDatabase: SongDatabase // DB 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        // RoomDB 초기화
        songDatabase = SongDatabase.getDatabase(requireContext())

        // 좋아요 곡 가져오기
        fetchLikedSongs()

        return binding.root
    }

    private fun fetchLikedSongs() {
        lifecycleScope.launch {
            val likedSongs = songDatabase.songDao().getLikedSongs() // 좋아요가 true인 곡 가져오기

            // Song -> SavedSong 변환
            val savedSongList = likedSongs.map { song ->
                // 앨범의 커버 이미지 리소스 ID 가져오기
                val albumCoverResId = songDatabase.albumDao().getAlbumById(song.albumIdx.toInt())?.coverImg?.toInt()
                    ?: R.drawable.songcover // 앨범 커버가 없을 경우 기본 이미지 사용

                SavedSong(
                    title = song.title,
                    artist = song.singer,
                    thumbnailResId = albumCoverResId
                )
            }.toMutableList()

            // Adapter 설정
            songAdapter = SavedSongAdapter(savedSongList) { position ->
                val song = likedSongs[position]
                updateLikeStatus(song, false) // 좋아요 해제
                fetchLikedSongs() // UI 업데이트
            }

            // RecyclerView 설정
            binding.recyclerView.adapter = songAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    // 좋아요 상태 변경 메서드
    private fun updateLikeStatus(song: Song, isLiked: Boolean) {
        lifecycleScope.launch {
            val updatedSong = song.copy(isLike = isLiked) // 좋아요 상태 변경
            songDatabase.songDao().updateSong(updatedSong)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}