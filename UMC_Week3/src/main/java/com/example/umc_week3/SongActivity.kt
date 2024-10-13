package com.example.umc_week3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week3.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MainActivity로부터 데이터를 받아 UI 업데이트
        binding.songTitle.text = intent.getStringExtra("trackTitle") ?: "Unknown Title"
        binding.artistName.text = intent.getStringExtra("artistName") ?: "Unknown Artist"
        binding.albumCover.setImageResource(intent.getIntExtra("albumCoverResId", R.drawable.songcover))

        // 재생 상태 초기 설정
        isPlaying = intent.getBooleanExtra("isPlaying", false)
        updatePlayPauseIcon()

        // 뒤로가기 버튼 클릭 시 MainActivity로 돌아가기
        binding.backIcon.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("isPlaying", isPlaying)
                putExtra("trackTitle", binding.songTitle.text.toString()) // 노래 제목 전달
            }
            setResult(RESULT_OK, resultIntent) // MainActivity로 결과 전달
            finish() // SongActivity 종료
        }

        // 재생/일시정지 버튼 클릭 처리
        binding.playPauseIcon.setOnClickListener {
            isPlaying = !isPlaying
            updatePlayPauseIcon()
        }
    }

    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.playPauseIcon.setImageResource(R.drawable.pause)
        } else {
            binding.playPauseIcon.setImageResource(R.drawable.ic_play)
        }
    }
}
