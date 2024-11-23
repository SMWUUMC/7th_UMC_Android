package com.example.umc_week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    private lateinit var songAdapter: SavedSongAdapter
    private lateinit var songList: MutableList<SavedSong>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)

        // 데이터 리스트 생성
        songList = mutableListOf(
            SavedSong("Dumb Dumb", "Red Velvet", R.drawable.songcover),
            SavedSong("Pink Venom", "BLACKPINK", R.drawable.cover3),
            SavedSong("Young, Dumb, Stupid", "NMIXX", R.drawable.cover4),
            SavedSong("Huff n Puff", "Red Velvet", R.drawable.songcover),
            SavedSong("Ready for Love", "BLACKPINK", R.drawable.cover3),
            SavedSong("Love Me Like This", "NMIXX", R.drawable.cover4),
            SavedSong("Campfire", "Red Velvet", R.drawable.songcover),
            SavedSong("PAXXWORD", "NMIXX", R.drawable.cover4),
            SavedSong("Red Dress", "Red Velvet", R.drawable.songcover),
            SavedSong("Just Did It", "NMIXX", R.drawable.cover4)
        )

        // 어댑터 설정 및 RecyclerView 연결
        songAdapter = SavedSongAdapter(songList) { position ->
            songList.removeAt(position)
            songAdapter.notifyItemRemoved(position)
            songAdapter.notifyItemRangeChanged(position, songList.size)
        }
        binding.songListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.songListRecyclerView.adapter = songAdapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
