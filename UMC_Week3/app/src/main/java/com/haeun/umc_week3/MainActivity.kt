package com.haeun.umc_week3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.haeun.umc_week3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    companion object {
        const val REQUEST_CODE_SONG = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerTitleTv.text.toString(), 0, 60, false )

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivityForResult(intent, REQUEST_CODE_SONG)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SONG && resultCode == Activity.RESULT_OK) {
            val albumTitle = data?.getStringExtra("albumTitle") ?: ""
            Toast.makeText(this, albumTitle, Toast.LENGTH_SHORT).show()
        }
    }



    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

//                R.id.lookFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frm, LookFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
//                R.id.searchFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frm, SearchFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2" , AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }



}
