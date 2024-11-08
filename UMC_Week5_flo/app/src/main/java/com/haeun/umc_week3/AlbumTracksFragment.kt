package com.haeun.umc_week3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment

class AlbumTracksFragment : Fragment() {

    private lateinit var mixButton: ImageView
    private var isMixOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album_tracks, container, false)
        mixButton = view.findViewById(R.id.song_mixoff_tg)

        // 취향 MIX 버튼 클릭 시 이미지 변경
        mixButton.setOnClickListener {
            isMixOn = !isMixOn
            if (isMixOn) {
                mixButton.setImageResource(R.drawable.btn_toggle_off) // MIX 활성화 이미지로 변경
            } else {
                mixButton.setImageResource(R.drawable.btn_toggle_on) // MIX 비활성화 이미지로 변경
            }
        }

        return view
    }
}



