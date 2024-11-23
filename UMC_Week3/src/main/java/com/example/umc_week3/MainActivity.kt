package com.example.umc_week3

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var currentSelectedIndex: Int = 0
    private lateinit var binding: ActivityMainBinding
    private var isPlaying = false
    private var isRepeat = false

    // SongActivity로부터 데이터 받기 처리
    private val songActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            isPlaying = data?.getBooleanExtra("isPlaying", false) ?: false
            isRepeat = data?.getBooleanExtra("isRepeat", false) ?: false

            val trackTitle = data?.getStringExtra("trackTitle") ?: "Unknown Title"
            val seekBarProgress = data?.getIntExtra("seekBarProgress", 0) ?: 0
            binding.mainSeekbar.progress = seekBarProgress
            Toast.makeText(this, "Now Playing: $trackTitle", Toast.LENGTH_SHORT).show()

            updatePlayPauseIcon()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 HomeFragment 설정
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // MusicPlayer 초기화 및 재생
        MusicPlayer.initPlayer(this)
        if (isPlaying) {
            MusicPlayer.play()
            MusicPlayer.startSeekBarUpdate()
        }

        val songLength = MusicPlayer.getDuration()
        binding.mainSeekbar.max = songLength

        // SeekBar 업데이트
        MusicPlayer.setSeekBarCallback { position ->
            binding.mainSeekbar.progress = position
        }

        // SeekBar 위치 변경 시 업데이트
        binding.mainSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                MusicPlayer.stopSeekBarUpdate()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                MusicPlayer.startSeekBarUpdate()
            }
        })

        // SongActivity로 이동
        binding.musicPlayerBar.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("trackTitle", binding.musicTitle.text.toString())
                putExtra("artistName", binding.musicArtist.text.toString())
                putExtra("albumCoverResId", R.drawable.songcover)
                putExtra("isPlaying", isPlaying)
                putExtra("isRepeat", isRepeat)
            }
            songActivityLauncher.launch(intent)
        }

        // 재생/일시정지 버튼 클릭
        binding.musicPlayPause.setOnClickListener {
            isPlaying = !isPlaying
            if (isPlaying) {
                MusicPlayer.play()
                MusicPlayer.startSeekBarUpdate()
            } else {
                MusicPlayer.pause()
                MusicPlayer.stopSeekBarUpdate()
            }
            updatePlayPauseIcon()
        }

        // 반복 재생 설정
        MusicPlayer.setOnCompletionListener {
            if (isRepeat) {
                MusicPlayer.seekTo(0)
                binding.mainSeekbar.progress = 0
                MusicPlayer.play()
            } else {
                isPlaying = false
                updatePlayPauseIcon()
            }
        }

        // BottomNavigationView 설정
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> { // 홈 메뉴 클릭 시
                    if (currentSelectedIndex != 0) {
                        currentSelectedIndex = 0
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, HomeFragment())
                            .commit()
                    }
                    true
                }
                R.id.storage -> { // 4번째 메뉴 클릭 시
                    if (currentSelectedIndex != 3) { // 4번째 메뉴 인덱스를 3으로 설정
                        currentSelectedIndex = 3
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, StorageFragment()) // StorageFragment로 이동
                            .commit()
                    }
                    true
                }
                else -> false // 다른 메뉴는 막아둠
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (isPlaying) {
            MusicPlayer.startSeekBarUpdate()
        }

        // MainActivity의 SeekBar 콜백 재설정
        MusicPlayer.setSeekBarCallback { position ->
            binding.mainSeekbar.progress = position
        }
    }

    override fun onPause() {
        super.onPause()
        MusicPlayer.stopSeekBarUpdate()
    }

    // 재생/일시정지 아이콘 업데이트 메서드
    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.musicPlayPause.setImageResource(R.drawable.pause)
        } else {
            binding.musicPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    // MiniPlayer 업데이트
    fun updateMiniPlayer(album: Album) {
        binding.musicTitle.text = album.trackList[0]
        binding.musicArtist.text = album.artist
        binding.musicThumbnail.setImageResource(album.coverResId)

        // 재생을 처음부터 시작하도록 설정
        MusicPlayer.initPlayer(this)  // MusicPlayer를 새로 초기화하여 처음부터 재생
        MusicPlayer.seekTo(0)          // 재생 위치를 처음으로 설정
        MusicPlayer.play()             // 음악 재생 시작
        MusicPlayer.startSeekBarUpdate() // SeekBar 업데이트 시작

        // 미니 플레이어가 재생 상태로 변경되었음을 표시
        isPlaying = true
        updatePlayPauseIcon()
    }
}
