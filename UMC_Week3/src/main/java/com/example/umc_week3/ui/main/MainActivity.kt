package com.example.umc_week3.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.umc_week3.ui.main.home.HomeFragment
import com.example.umc_week3.ui.main.look.LookFragment
import com.example.umc_week3.utils.MusicPlayer
import com.example.umc_week3.R
import com.example.umc_week3.ui.song.SongActivity
import com.example.umc_week3.data.local.SongDatabase
import com.example.umc_week3.ui.main.storage.StorageFragment
import com.example.umc_week3.data.entities.Album
import com.example.umc_week3.data.entities.Song
import com.example.umc_week3.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var currentSelectedIndex: Int = 0
    private lateinit var binding: ActivityMainBinding
    private var isPlaying = false
    private var isRepeat = false

    private lateinit var songDatabase: SongDatabase // RoomDB 인스턴스
    private val sharedPreferences by lazy { getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE) }

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

        // Room Database 초기화
        songDatabase = Room.databaseBuilder(
            applicationContext,
            SongDatabase::class.java,
            "song_database"
        )
            .fallbackToDestructiveMigration() // 기존 데이터를 삭제하고 새로 생성
            .build()
        Log.d("AppDebug", "Room Database Initialized: $songDatabase")

        inputDummyData()

        lifecycleScope.launch {
            val albums = songDatabase.albumDao().getAllAlbums()
            val songs = songDatabase.songDao().getAllSongs()
            Log.d("RoomDB", "Albums: $albums")
            Log.d("RoomDB", "Songs: $songs")
        }



        // RoomDB에서 곡 데이터 로드 및 MiniPlayer 업데이트
        lifecycleScope.launch {
            val savedSongId = getSongIdFromPreferences()
            val song = if (savedSongId != -1) {
                songDatabase.songDao().getSongById(savedSongId)
            } else {
                songDatabase.songDao().getAllSongs().firstOrNull()
            }

            if (song != null) {
                updateMiniPlayerWithSong(song)
            } else {
                Toast.makeText(this@MainActivity, "곡 데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "getSongById($savedSongId) returned null or no songs available.")
            }
        }

        loadMiniPlayerData()
        setupMusicPlayer()


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

                R.id.explore -> { // 2번째 메뉴 클릭 시
                    if (currentSelectedIndex != 1) { // 4번째 메뉴 인덱스를 1으로 설정
                        currentSelectedIndex = 1
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                LookFragment()
                            ) // LookFragment로 이동
                            .commit()
                    }
                    true
                }

                R.id.storage -> { // 4번째 메뉴 클릭 시
                    if (currentSelectedIndex != 3) { // 4번째 메뉴 인덱스를 3으로 설정
                        currentSelectedIndex = 3
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                StorageFragment()
                            ) // StorageFragment로 이동
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
        // DB에서 현재 곡 정보를 다시 로드하여 MiniPlayer 업데이트
        loadMiniPlayerData()
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


    private fun setupMusicPlayer() {
        // MusicPlayer 초기화 및 재생
        MusicPlayer.setSeekBarCallback { position ->
            binding.mainSeekbar.progress = position
        }

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


        binding.musicPlayerBar.setOnClickListener {
            // Intent로 SongActivity 실행
            val intent = Intent(this, SongActivity::class.java)
            val currentSongId = getSongIdFromPreferences() // 현재 재생 중인 곡 ID를 가져옵니다.

            lifecycleScope.launch {
                val currentSong = songDatabase.songDao().getSongById(currentSongId)
                if (currentSong != null) {
                    intent.putExtra("songId", currentSongId) // 현재 곡 ID 전달
                    intent.putExtra("albumIdx", currentSong.albumIdx) // 현재 곡의 앨범 인덱스 전달
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "현재 곡 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }


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

        binding.musicPrev.setOnClickListener {
            moveSong(-1) // 이전 곡
        }

        binding.musicNext.setOnClickListener {
            moveSong(1) // 다음 곡
        }

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
    }

    private fun updatePlayPauseIcon() {
        if (isPlaying) {
            binding.musicPlayPause.setImageResource(R.drawable.pause)
        } else {
            binding.musicPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun loadMiniPlayerData() {
        lifecycleScope.launch {
            val savedSongId = getSongIdFromPreferences()
            val currentSong = savedSongId.takeIf { it != -1 }?.let {
                songDatabase.songDao().getSongById(it)
            }

            if (currentSong != null) {
                // MiniPlayer 및 MusicPlayer 업데이트
                updateMiniPlayerWithSong(currentSong)

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
                Log.w("MainActivity", "restoreCurrentSongState: 현재 곡 데이터를 찾을 수 없습니다.")
            }
        }
    }

    private fun saveCurrentSongState() {
        lifecycleScope.launch {
            val savedSongId = getSongIdFromPreferences() // 현재 곡 ID를 가져옴
            val currentSong = savedSongId.takeIf { it != -1 }?.let {
                songDatabase.songDao().getSongById(it)
            }

            if (currentSong != null) {
                // 현재 곡 상태를 업데이트
                currentSong.isPlaying = isPlaying
                currentSong.second = MusicPlayer.getCurrentPosition() / 1000 // 현재 재생 위치(초)

                // RoomDB 업데이트
                songDatabase.songDao().updateSong(currentSong)
            } else {
                Log.w("MainActivity", "saveCurrentSongState: 현재 곡 데이터를 찾을 수 없습니다.")
            }
        }
    }

    fun updateMiniPlayerWithSong(song: Song) {
        lifecycleScope.launch {
            binding.musicTitle.text = song.title
            binding.musicArtist.text = song.singer
            val album = songDatabase.albumDao().getAlbumById(song.albumIdx.toInt())
            album?.coverImg?.let { binding.musicThumbnail.setImageResource(it) }

            // MusicPlayer 상태 초기화
            MusicPlayer.initPlayer(this@MainActivity, song.id)
            binding.mainSeekbar.max = song.playtime * 1000

            // 재생/멈춤 상태 적용
            if (song.isPlaying) {
                MusicPlayer.play()
                MusicPlayer.startSeekBarUpdate()
            } else {
                MusicPlayer.pause()
                MusicPlayer.stopSeekBarUpdate()
            }

            // SeekBar 초기 위치 설정
            MusicPlayer.seekTo(song.second * 1000)
            binding.mainSeekbar.progress = song.second * 1000
            updatePlayPauseIcon(song.isPlaying)

            saveSongIdToPreferences(song.id)
        }
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        if (isPlaying) {
            binding.musicPlayPause.setImageResource(R.drawable.pause)
        } else {
            binding.musicPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    // 더미 데이터 삽입
    private fun inputDummyData() {
        lifecycleScope.launch {
            val albumDao = songDatabase.albumDao()
            val songDao = songDatabase.songDao()

            // 중복 삽입 방지
            if (albumDao.getAllAlbums().isNotEmpty() && songDao.getAllSongs().isNotEmpty()) {
                Log.d("DummyData", "Albums and songs already exist in the database.")
                return@launch
            }

            // 1. 앨범 데이터 삽입
            val albumMap = mutableMapOf<String, Long>()
            val albums = listOf(
                Album(
                    title = "The Red",
                    singer = "Red Velvet",
                    releaseDate = "2015.09.09",
                    type = "정규",
                    genre = "댄스",
                    coverImg = R.drawable.songcover
                ),
                Album(
                    title = "Pink Venom",
                    singer = "BLACKPINK",
                    releaseDate = "2022.08.19",
                    type = "싱글",
                    genre = "힙합, EDM, 팝 랩",
                    coverImg = R.drawable.cover3
                ),
                Album(
                    title = "expérgo",
                    singer = "NMIXX",
                    releaseDate = "2023.03.20",
                    type = "EP",
                    genre = "팝",
                    coverImg = R.drawable.cover4
                )
            )

            albums.forEach { album ->
                val albumId = albumDao.insertAlbum(album)
                Log.d("DummyData", "Inserted album: $album with ID: $albumId")
                albumMap[album.title] = albumId
            }

            // 2. 곡 데이터 삽입
            val songs = listOf(
                // The Red 앨범
                Song(title = "Dumb Dumb", singer = "Red Velvet", second = 0, playtime = 203, isPlaying = false, music = R.raw.dumb_dumb, isLike = false, albumIdx = albumMap["The Red"] ?: return@launch),
                Song(title = "Huff n Puff", singer = "Red Velvet", second = 0, playtime = 181, isPlaying = false, music = R.raw.huff_n_puff, isLike = false, albumIdx = albumMap["The Red"]?: return@launch),
                Song(title = "Campfire", singer = "Red Velvet", second = 0, playtime = 197, isPlaying = false, music = R.raw.campfire, isLike = false, albumIdx = albumMap["The Red"]?: return@launch),

                // Pink Venom 앨범
                Song(title = "Pink Venom", singer = "BLACKPINK", second = 0, playtime = 188, isPlaying = false, music = R.raw.pink_venom, isLike = false, albumIdx = albumMap["Pink Venom"]?: return@launch),
                Song(title = "Ready for Love", singer = "BLACKPINK", second = 0, playtime = 184, isPlaying = false, music = R.raw.ready_for_love, isLike = false, albumIdx = albumMap["Pink Venom"]?: return@launch),

                // expérgo 앨범
                Song(title = "Young, Dumb, Stupid", singer = "NMIXX", second = 0, playtime = 190, isPlaying = false, music = R.raw.young_dumb_stupid, isLike = false, albumIdx = albumMap["expérgo"]?: return@launch),
                Song(title = "Love Me Like This", singer = "NMIXX", second = 0, playtime = 190, isPlaying = false, music = R.raw.love_me_like_this, isLike = false, albumIdx = albumMap["expérgo"]?: return@launch),
                Song(title = "PAXXWORD", singer = "NMIXX", second = 0, playtime = 192, isPlaying = false, music = R.raw.paxxword, isLike = false, albumIdx = albumMap["expérgo"]?: return@launch)
            )

            songs.forEach { song ->
                songDao.insertSong(song)
                Log.d("DummyData", "Inserted song: $song")
            }
        }
    }

    private fun moveSong(direction: Int) {
        lifecycleScope.launch {
            val currentSongId = getSongIdFromPreferences()
            val currentSong = songDatabase.songDao().getSongById(currentSongId)

            if (currentSong == null) {
                Toast.makeText(this@MainActivity, "현재 곡 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // 현재 앨범에 속한 곡들 가져오기
            val albumSongs = songDatabase.songDao().getSongsByAlbumId(currentSong.albumIdx.toInt())

            // 현재 곡의 인덱스 찾기
            val currentIndex = albumSongs.indexOfFirst { it.id == currentSongId }
            if (currentIndex == -1) {
                Toast.makeText(this@MainActivity, "현재 곡이 해당 앨범에 없습니다.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // 새 인덱스 계산
            val newIndex = currentIndex + direction
            if (newIndex < 0) {
                Toast.makeText(this@MainActivity, "첫 번째 곡입니다.", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (newIndex >= albumSongs.size) {
                Toast.makeText(this@MainActivity, "마지막 곡입니다.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val newSong = albumSongs[newIndex]
            saveSongIdToPreferences(newSong.id)

            // 현재 곡의 상태 초기화
            songDatabase.songDao().updateIsPlayingById(currentSongId, false)
            songDatabase.songDao().updateSecondById(currentSongId, 0)

            // 새 곡의 상태 설정
            songDatabase.songDao().updateIsPlayingById(newSong.id, true)
            songDatabase.songDao().updateSecondById(newSong.id, 0)

            // MiniPlayer 업데이트
            updateMiniPlayerWithSong(newSong)
        }
    }

    private fun saveSongIdToPreferences(songId: Int) {
        sharedPreferences.edit()
            .putInt("songId", songId)
            .apply()
    }

    private fun getSongIdFromPreferences(): Int {
        return sharedPreferences.getInt("songId", -1)
    }

}
