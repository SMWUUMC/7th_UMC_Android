package com.example.umc_week3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.umc_week3.databinding.ActivitySongBinding
import kotlinx.coroutines.launch

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private var isPlaying = false
    private var isRepeat = false
    private lateinit var songDatabase: SongDatabase
    private var albumIdx: Long = -1 // 현재 앨범의 인덱스
    private var songId: Int = -1 // 현재 곡의 ID
    private var songs = mutableListOf<Song>() // 모든 곡 리스트
    private var nowPos: Int = 0 // 현재 곡의 위치 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDatabase = SongDatabase.getDatabase(this)


        // Intent로 전달받은 곡 ID
        songId = intent.getIntExtra("songId", -1)
        albumIdx = intent.getLongExtra("albumIdx", -1)


        if (albumIdx == -1L) {
            showToast("앨범 정보를 가져올 수 없습니다.")
            finish()
            return
        }

        // 해당 앨범의 곡 데이터 로드
        lifecycleScope.launch {
            songs = songDatabase.songDao().getSongsByAlbumId(albumIdx.toInt()).toMutableList()
            nowPos = songs.indexOfFirst { it.id == songId }
            if (nowPos != -1) {
                setupUIWithSong(songs[nowPos])
            } else {
                showToast("곡 정보를 불러올 수 없습니다.")
                finish()
            }
        }

        // MusicPlayer 완료 리스너
        MusicPlayer.setOnCompletionListener {
            if (isRepeat) {
                MusicPlayer.seekTo(0)
                MusicPlayer.play()
                binding.seekBar.progress = 0
            } else {
                moveSong(1) // 다음 곡으로 이동
            }
        }

        // SeekBar와 currentTime 업데이트 콜백 설정
        MusicPlayer.setSeekBarCallback { position ->
            binding.seekBar.progress = position
            binding.currentTime.text = formatTime(position / 1000)
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

        // 이전 곡 버튼 클릭
        binding.prevIcon.setOnClickListener {
            moveSong(-1) // 이전 곡으로 이동
        }

        // 다음 곡 버튼 클릭
        binding.nextIcon.setOnClickListener {
            moveSong(1) // 다음 곡으로 이동
        }

        // 뒤로가기 아이콘 클릭
        binding.backIcon.setOnClickListener {
            saveCurrentSongState()
            finish()
        }

        // 좋아요 버튼 클릭
        binding.likeIcon.setOnClickListener {
            toggleLike()
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
        restoreCurrentSongState()

        // 현재 위치를 즉시 SeekBar와 TextView에 반영
        val currentPosition = MusicPlayer.getCurrentPosition()
        binding.seekBar.progress = currentPosition
        binding.currentTime.text = formatTime(currentPosition / 1000)
    }

    override fun onPause() {
        super.onPause()
        saveCurrentSongState()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveCurrentSongState()
        MusicPlayer.release()
    }

    private fun restoreCurrentSongState() {
        lifecycleScope.launch {
            val currentSong = songId.takeIf { it != -1 }?.let {
                songDatabase.songDao().getSongById(it)
            }

            if (currentSong != null) {
                setupUIWithSong(currentSong)

                // MusicPlayer 재생 상태 복원
                MusicPlayer.seekTo(currentSong.second * 1000)
                if (currentSong.isPlaying) {
                    MusicPlayer.play()
                    MusicPlayer.startSeekBarUpdate()
                } else {
                    MusicPlayer.pause()
                    MusicPlayer.stopSeekBarUpdate()
                }
            } else {
                showToast("곡 데이터를 불러올 수 없습니다.")
                Log.w("SongActivity", "restoreCurrentSongState: 현재 곡 데이터를 찾을 수 없습니다.")
            }
        }
    }

    private fun setupUIWithSong(song: Song) {
        lifecycleScope.launch {
            // MusicPlayer 초기화
            MusicPlayer.initPlayer(this@SongActivity, song.id)

            // UI 업데이트
            binding.songTitle.text = song.title
            binding.artistName.text = song.singer

            // 앨범 커버 업데이트
            val album = songDatabase.albumDao().getAlbumById(song.albumIdx.toInt())
            val coverImgResId = album?.coverImg ?: R.drawable.songcover
            binding.albumCover.setImageResource(coverImgResId)

            // 좋아요 상태 반영
            updateLikeIcon(song.isLike)

            // SeekBar 초기화
            binding.seekBar.progress = 0
            binding.currentTime.text = formatTime(0) // 0초로 초기화
            waitForMediaPlayerReady {
                binding.seekBar.max = MusicPlayer.getDuration() // MediaPlayer의 총 재생 시간
                binding.totalTime.text = formatTime(MusicPlayer.getDuration() / 1000)
            }

            isPlaying = song.isPlaying
            updatePlayPauseIcon()

            // MusicPlayer 완료 리스너
            MusicPlayer.setOnCompletionListener {
                if (isRepeat) {
                    MusicPlayer.seekTo(0)
                    MusicPlayer.play()
                    binding.seekBar.progress = 0
                } else {
                    // 마지막 곡에서 멈춤 상태로 설정
                    if (nowPos == songs.size - 1) {
                        isPlaying = false
                        MusicPlayer.pause()
                        binding.seekBar.progress = binding.seekBar.max // SeekBar를 끝으로 설정
                        updatePlayPauseIcon()
                    } else {
                        moveSong(1) // 다음 곡으로 이동
                    }
                }
            }

            MusicPlayer.setSeekBarCallback { position ->
                binding.seekBar.progress = position
                binding.currentTime.text = formatTime(position / 1000)
            }

            // 재생/멈춤 상태에 따라 동작 설정
            if (isPlaying) {
                MusicPlayer.play()
                MusicPlayer.startSeekBarUpdate()
            } else {
                MusicPlayer.pause()
                MusicPlayer.stopSeekBarUpdate()
            }
        }
    }

    private fun moveSong(direction: Int) {
        val newPos = nowPos + direction
        if (newPos < 0) {
            showToast("첫 번째 곡입니다.")
            return
        }
        if (newPos >= songs.size) {
            showToast("마지막 곡입니다.")
            return
        }

        // 이전 곡의 상태 저장
        saveCurrentSongState()

        // 새로운 곡으로 전환
        nowPos = newPos
        val newSong = songs[nowPos]

        // MediaPlayer 리소스 해제
        MusicPlayer.release()

        // 전환 직전 상태에 따라 새 곡의 재생/멈춤 설정
        newSong.isPlaying = isPlaying // 전환 직전 재생 상태 반영
        if (!isPlaying) {
            newSong.second = 0 // 멈춤 상태라면 새 곡도 0:00에서 멈춤
        }

        // 새 곡 UI와 Player 업데이트
        setupUIWithSong(newSong)
    }

    private fun waitForMediaPlayerReady(onReady: () -> Unit) {
        lifecycleScope.launch {
            while (MusicPlayer.getDuration() == 0) {
                // MediaPlayer가 준비될 때까지 대기
                kotlinx.coroutines.delay(100) // 100ms 간격으로 확인
            }
            onReady() // 준비 완료 후 콜백 실행
        }
    }

    private fun toggleLike() {
        val currentSong = songs[nowPos]
        currentSong.isLike = !currentSong.isLike

        lifecycleScope.launch {
            songDatabase.songDao().updateIsLikeById(currentSong.isLike, currentSong.id)
        }

        updateLikeIcon(currentSong.isLike)
    }

    private fun updateLikeIcon(isLiked: Boolean) {
        val color = if (isLiked) R.color.indicator_selected else R.color.gray
        binding.likeIcon.setColorFilter(
            ContextCompat.getColor(this, color),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun saveCurrentSongState() {
        lifecycleScope.launch {
            val currentSong = Song(
                id = songs[nowPos].id,
                title = binding.songTitle.text.toString(),
                singer = binding.artistName.text.toString(),
                second = 0,
                playtime = MusicPlayer.getDuration() / 1000,
                isPlaying = isPlaying,
                music = songs[nowPos].music,
                isLike = songs[nowPos].isLike,
                albumIdx = songs[nowPos].albumIdx
            )
            songDatabase.songDao().updateSong(currentSong)
        }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}