package com.haeun.umc_week3

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.haeun.umc_week3.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRandomIv.setOnClickListener {
            setRandomStatus(true)
        }

        binding.songNotrandomIv.setOnClickListener {
            setRandomStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(true)
        }

        binding.songRepeat2Iv.setOnClickListener {
            setRepeatStatus(false)
        }

        // 전달받은 데이터 가져오기
        val title = intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")

        // 제목과 가수명을 SongActivity UI에 설정
        binding.songMusicTitleTv.text = title
        binding.songSingerNameTv.text = artist

        binding.songDownIb.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("albumTitle", intent.getStringExtra("title"))
            setResult(Activity.RESULT_OK, resultIntent) // 결과 설정
            finish() // SongActivity 종료 및 결과 전달
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }


    fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    fun setRandomStatus(isRandom : Boolean) {

        if(isRandom) {
            binding.songRandomIv.visibility = View.GONE
            binding.songNotrandomIv.visibility = View.VISIBLE
        }
        else {
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songNotrandomIv.visibility = View.GONE
        }
    }

    fun setRepeatStatus(isRepeat : Boolean) {

        if(isRepeat) {
            binding.songRepeatIv.visibility = View.GONE
            binding.songRepeat2Iv.visibility = View.VISIBLE
        }
        else {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeat2Iv.visibility = View.GONE
        }
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){

                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }


}
