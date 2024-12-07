package com.example.umc_week3.ui.main.album.bside

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.data.local.SongDatabase
import com.example.umc_week3.databinding.FragmentBsideBinding
import kotlinx.coroutines.launch

class BsideFragment : Fragment() {

    private var _binding: FragmentBsideBinding? = null
    private val binding get() = _binding!!

    private var albumIdx: Int = -1 // 전달받은 albumIdx
    private lateinit var songDatabase: SongDatabase // RoomDB 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBsideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RoomDB 초기화
        songDatabase = SongDatabase.getDatabase(requireContext())

        // arguments에서 albumIdx 받기
        albumIdx = arguments?.getInt("albumIdx") ?: -1

        if (albumIdx != -1) {
            fetchTrackList()
        } else {
            Log.e("BsideFragment", "Invalid albumIdx passed to fragment")
        }
    }

    private fun fetchTrackList() {
        lifecycleScope.launch {
            // albumIdx로 RoomDB에서 곡 데이터 조회
            val songs = songDatabase.songDao().getSongsByAlbumId(albumIdx)

            if (songs.isNotEmpty()) {
                val trackList = songs.map { it.title } // 수록곡 제목 리스트
                val artistName = songs.first().singer // 모든 곡의 가수는 동일하다고 가정

                Log.d("BsideFragment", "Fetched Tracks: $trackList")

                // RecyclerView에 수록곡 리스트 표시
                val adapter = TrackListAdapter(trackList, artistName)
                binding.recyclerViewTracks.adapter = adapter
                binding.recyclerViewTracks.layoutManager = LinearLayoutManager(context)
            } else {
                Log.e("BsideFragment", "No songs found for albumIdx: $albumIdx")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(albumIdx: Int): BsideFragment {
            val fragment = BsideFragment()
            val args = Bundle().apply {
                putInt("albumIdx", albumIdx)
            }
            fragment.arguments = args
            return fragment
        }
    }
}