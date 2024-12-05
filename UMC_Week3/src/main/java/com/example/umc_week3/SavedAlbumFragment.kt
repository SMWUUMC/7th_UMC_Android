package com.example.umc_week3

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.databinding.FragmentSavedAlbumBinding
import kotlinx.coroutines.launch

class SavedAlbumFragment : Fragment() {

    private var _binding: FragmentSavedAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var albumAdapter: SavedAlbumAdapter
    private lateinit var songDatabase: SongDatabase // RoomDatabase 인스턴스
    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)

        // RecyclerView 설정
        albumAdapter = SavedAlbumAdapter(emptyList()) { album ->
            toggleAlbumLike(album) // 클릭 이벤트로 좋아요 해제
        }

        binding.savedAlbumsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.savedAlbumsRecyclerView.adapter = albumAdapter

        // RoomDatabase 초기화
        songDatabase = SongDatabase.getDatabase(requireContext())

        // 데이터 로드
        loadSavedAlbums()

        return binding.root
    }


    fun loadSavedAlbums() {
        val userId = getCurrentUserId()
        if (userId == -1) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val likes = songDatabase.likeDao().getLikesByUser(userId)

                val likedAlbums = likes.mapNotNull {
                    val album = songDatabase.albumDao().getAlbumById(it.albumId)
                    album
                }

                if (likedAlbums.isEmpty()) {
                }

                albumAdapter.updateData(likedAlbums)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "저장된 앨범을 불러오는 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun toggleAlbumLike(album: Album) {
        val userId = getCurrentUserId()
        lifecycleScope.launch {
            songDatabase.likeDao().deleteLike(userId, album.id)
            loadSavedAlbums() // 데이터 갱신
        }
    }

    private fun getCurrentUserId(): Int {
        val userId = sharedPreferences.getInt("currentUserId", -1)
        return userId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}