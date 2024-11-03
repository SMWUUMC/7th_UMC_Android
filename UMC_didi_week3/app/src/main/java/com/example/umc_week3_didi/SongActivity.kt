package com.example.umc_week3_didi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week3_didi.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MusicPlayer.initPlayer(this) // MediaPlayer 초기화

        // MainActivity로부터 데이터를 받아 UI 업데이트
        binding.songTitle.text = intent.getStringExtra("trackTitle") ?: "Unknown Title"
        binding.artistName.text = intent.getStringExtra("artistName") ?: "Unknown Artist"
        binding.albumCover.setImageResource(
            intent.getIntExtra(
                "albumCoverResId",
                R.drawable.songcover
            )
        )

        // 재생 상태 초기 설정
        isPlaying = intent.getBooleanExtra("isPlaying", false)
        updatePlayPauseIcon()

        var songLength = MusicPlayer.getDuration()
        binding.seekBar.max = songLength
        binding.totalTime.text = formatTime(songLength / 1000)
        isPlaying = MusicPlayer.isPlaying()
        updatePlayPauseIcon()

        // 재생/일시정지 버튼 클릭 처리
        binding.playPauseIcon.setOnClickListener {
            if (isPlaying) {
                MusicPlayer.pause()
            } else {
                MusicPlayer.play()
            }
            isPlaying = !isPlaying
            updatePlayPauseIcon()
        }

        // 뒤로가기 버튼 클릭 시 MainActivity로 돌아가기
        binding.backIcon.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("isPlaying", isPlaying)
                putExtra("trackTitle", binding.songTitle.text.toString()) // 노래 제목 전달
            }
            setResult(RESULT_OK, resultIntent) // MainActivity로 결과 전달
            finish() // SongActivity 종료
        }

        // SeekBar 변경 시 음악 재생 위치 변경
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { // 사용자에 의한 변경 - 재생 위치 이동 및 시간 텍스트 업데이트
                    MusicPlayer.seekTo(progress)
                    binding.currentTime.text = formatTime(progress / 1000)
                }
            }

            // 사용자가 SeekBar를 조작할 때 일시정지
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (isPlaying) {
                    MusicPlayer.pause()
                    updatePlayPauseIcon() // 일시정지 아이콘 표시
                }
                MusicPlayer.stopSeekBarUpdate()
            }

            // 사용자가 SeekBar 조작을 멈추면 다시 재생
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (isPlaying) { // 현재 재생 상태일 때만 재생 시작
                    MusicPlayer.play()
                    MusicPlayer.startSeekBarUpdate()
                    updatePlayPauseIcon() // 재생 아이콘 표시
                }
            }
        })


        // SeekBar의 위치를 업데이트하는 콜백 설정
        MusicPlayer.setSeekBarCallback { position ->
            if (position >= songLength && !MusicPlayer.isPlaying()) {
                isPlaying = false
                runOnUiThread {
                    updatePlayPauseIcon() // UI 상태를 재생 아이콘으로 변경
                }
            }
            binding.seekBar.progress = position
            binding.currentTime.text = formatTime(position / 1000)
        }

    }



    // 재생/일시정지 버튼 클릭 처리
    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.playPauseIcon.setImageResource(R.drawable.pause)
        } else {
            binding.playPauseIcon.setImageResource(R.drawable.ic_play)
        }
    }

    // 시간 텍스트 형식 설정
    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        // MusicPlayer 해제는 SongActivity에서 하지 않음 - MainActivity와의 동기화를 위해 설정
    }


}


