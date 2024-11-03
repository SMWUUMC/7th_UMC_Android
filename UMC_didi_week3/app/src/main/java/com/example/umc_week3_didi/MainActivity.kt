package com.example.umc_week3_didi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week3_didi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var currentSelectedIndex: Int = 0
    private lateinit var binding: ActivityMainBinding
    private var isPlaying = false

    // registerForActivityResult - SongActivity에서 데이터 받기 처리
    private val songActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            isPlaying = data?.getBooleanExtra("isPlaying", false) ?: false
            updatePlayPauseIcon()

            // 노래 제목 받아서 Toast로 표시
            val trackTitle = data?.getStringExtra("trackTitle") ?: "Unknown Title"
            Toast.makeText(this, "Now Playing: $trackTitle", Toast.LENGTH_SHORT).show()
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

        // 음악 재생 바 클릭 시 SongActivity로 이동
        binding.musicPlayerBar.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("trackTitle", binding.musicTitle.text.toString())
                putExtra("artistName", binding.musicArtist.text.toString())
                putExtra("albumCoverResId", R.drawable.songcover)
                putExtra("isPlaying", isPlaying)
            }
            songActivityLauncher.launch(intent) // SongActivity 실행
        }

        // 재생/일시정지 버튼 클릭 처리
        binding.musicPlayPause.setOnClickListener {
            isPlaying = !isPlaying
            updatePlayPauseIcon()
        }

        // BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.home) {
                if (currentSelectedIndex != 0) {
                    currentSelectedIndex = 0
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                true
            } else { false }
        }
    }

    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.musicPlayPause.setImageResource(R.drawable.pause)
        } else {
            binding.musicPlayPause.setImageResource(R.drawable.ic_play)
        }
    }
}
