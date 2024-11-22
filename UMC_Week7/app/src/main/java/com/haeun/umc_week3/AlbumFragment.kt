package com.haeun.umc_week3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.haeun.umc_week3.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        val gson = Gson()

        val album = gson.fromJson(albumData, Album::class.java)
        //isLiked = isLikedAlbum(album.id)

        setViews(album)
        initViewPager()
        setClickListeners(album)


        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg!!)

        if(isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {
        //val userId: Int = getJwt()

//        binding.albumLikeIv.setOnClickListener {
//            if(isLiked) {
//                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
//                disLikeAlbum(userId, album.id)
//            } else {
//                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
//                likeAlbum(userId, album.id)
//            }
//
//            isLiked = !isLiked
//        }

        //set click listener
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initViewPager() {
        //init viewpager
        val albumAdapter = AlbumPagerAdapter(this)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }


//    private fun isLikedAlbum(albumId: Int): Boolean {
//        val songDB = SongDatabase.getInstance(requireContext())!!
//        val userId = getJwt()
//
//        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)
//
//        return likeId != null
//    }

//    private fun getJwt(): Int {
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//        val jwt = spf!!.getInt("jwt", 0)
//        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")
//
//        return jwt
//    }
}