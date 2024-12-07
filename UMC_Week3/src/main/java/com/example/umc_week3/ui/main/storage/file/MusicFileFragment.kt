package com.example.umc_week3.ui.main.storage.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_week3.R

class MusicFileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout for music files
        val view = inflater.inflate(R.layout.fragment_music_file, container, false)

        // 파일 목록 데이터 및 RecyclerView 설정 로직 작성
        return view
    }
}