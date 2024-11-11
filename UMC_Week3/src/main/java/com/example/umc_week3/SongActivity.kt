package com.example.umc_week3

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_week3.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private var isPlaying = false
    private var isRepeat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MusicPlayer.initPlayer(this)

        // Intent로 전달받은 정보 설정
        binding.songTitle.text = intent.getStringExtra("trackTitle") ?: "Unknown Title"
        binding.artistName.text = intent.getStringExtra("artistName") ?: "Unknown Artist"
        binding.albumCover.setImageResource(
            intent.getIntExtra("albumCoverResId", R.drawable.songcover)
        )

        // SeekBar 초기화
        val currentPosition = MusicPlayer.getCurrentPosition()
        binding.seekBar.progress = currentPosition
        binding.currentTime.text = formatTime(currentPosition / 1000)

        // 기타 설정들
        isPlaying = intent.getBooleanExtra("isPlaying", false)
        isRepeat = intent.getBooleanExtra("isRepeat", false)
        updatePlayPauseIcon()
        updateRepeatIcon()

        val songLength = MusicPlayer.getDuration()
        binding.seekBar.max = songLength
        binding.totalTime.text = formatTime(songLength / 1000)

        MusicPlayer.setOnCompletionListener {
            if (isRepeat) {
                MusicPlayer.seekTo(0)
                MusicPlayer.play()
                binding.seekBar.progress = 0
            } else {
                isPlaying = false
                updatePlayPauseIcon() // 아이콘을 멈춤 상태로 업데이트
            }
        }

        // SeekBar와 currentTime 업데이트 콜백 설정
        MusicPlayer.setSeekBarCallback { position ->
            binding.seekBar.progress = position
            binding.currentTime.text = formatTime(position / 1000)
        }

        if (isPlaying) {
            MusicPlayer.play()
        } else {
            MusicPlayer.pause()
        }

        // 재생/일시정지 버튼 클릭
        binding.playPauseIcon.setOnClickListener {
            isPlaying = !isPlaying
            if (isPlaying) {
                MusicPlayer.play()
            } else {
                MusicPlayer.pause()
            }
            updatePlayPauseIcon()
        }

        // 반복 재생 아이콘 클릭 시 반복 재생 설정
        binding.repeatIcon.setOnClickListener {
            isRepeat = !isRepeat
            updateRepeatIcon()
            MusicPlayer.setLooping(isRepeat) // MusicPlayer에 반복 재생 설정 전달
        }

        // 뒤로가기 아이콘 클릭
        binding.backIcon.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("isPlaying", isPlaying)
                putExtra("isRepeat", isRepeat)
                putExtra("trackTitle", binding.songTitle.text.toString())
                putExtra("seekBarProgress", binding.seekBar.progress)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // SeekBar 변경 리스너
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayer.seekTo(progress)
                    binding.currentTime.text = formatTime(progress / 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (isPlaying) {
                    MusicPlayer.pause()
                    updatePlayPauseIcon()
                }
                MusicPlayer.stopSeekBarUpdate()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (isPlaying) {
                    MusicPlayer.play()
                    MusicPlayer.startSeekBarUpdate()
                    updatePlayPauseIcon()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (isPlaying) {
            MusicPlayer.startSeekBarUpdate()
        }

        // 현재 위치를 즉시 SeekBar와 TextView에 반영
        val currentPosition = MusicPlayer.getCurrentPosition()
        binding.seekBar.progress = currentPosition
        binding.currentTime.text = formatTime(currentPosition / 1000)
    }

    private fun updateRepeatIcon() {
        if (isRepeat) {
            binding.repeatIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))
        } else {
            binding.repeatIcon.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        }
    }

    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.playPauseIcon.setImageResource(R.drawable.pause)
        } else {
            binding.playPauseIcon.setImageResource(R.drawable.ic_play)
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }
}
