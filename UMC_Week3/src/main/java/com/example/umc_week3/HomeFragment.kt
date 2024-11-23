package com.example.umc_week3

import BannerItem
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentPage = 0
    private val delayMillis: Long = 4000 // 4초마다 슬라이드

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 배너 데이터 샘플
        val bannerData = listOf(
            BannerItem(
                title = "포근하게 덮어주는 꿈의 \n목소리",
                subtitle = "총 15곡 2024.10.11",
                albumCover1 = R.drawable.cover2,
                albumTitle1 = "곡 제목 1",
                artist1 = "아티스트 1",
                albumCover2 = R.drawable.cover2,
                albumTitle2 = "곡 제목 2",
                artist2 = "아티스트 2",
                bannerImage = R.drawable.banner
            ),
            BannerItem(
                title = "여유로운 하루를 위한 \n재즈",
                subtitle = "총 12곡 2024.10.12",
                albumCover1 = R.drawable.cover2,
                albumTitle1 = "곡 제목 3",
                artist1 = "아티스트 3",
                albumCover2 = R.drawable.cover4,
                albumTitle2 = "곡 제목 4",
                artist2 = "아티스트 4",
                bannerImage = R.drawable.banner2
            )
        )

        // 배너 어댑터 설정
        bannerAdapter = BannerAdapter(bannerData)
        binding.viewPager.adapter = bannerAdapter
        binding.viewPager.offscreenPageLimit = 3

        // 인디케이터 설정
        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(binding.viewPager)

        // 자동 슬라이드 설정
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (currentPage == bannerAdapter.itemCount) {
                    currentPage = 0
                }
                binding.viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, delayMillis)
            }
        }
        handler.postDelayed(runnable, delayMillis)

        // 앨범 데이터 샘플
        val albums = listOf(
            Album(
                coverResId = R.drawable.songcover,
                title = "The Red",
                artist = "Red Velvet",
                info = "2015.09.09 | 정규 | 댄스",
                trackList = listOf("Dumb Dumb", "Huff n Puff", "Campfire", "Red Dress", "Oh Boy", "Lady's Room", "Time Slip", "Don't U Wait No More", "Day 1", "Cool World")
            ),
            Album(
                coverResId = R.drawable.cover3,
                title = "Pink Venom",
                artist = "BLACKPINK",
                info = "2022.08.19 | 싱글 | 힙합, EDM, 팝 랩",
                trackList = listOf("Pink Venom", "Ready for Love")
            ),
            Album(
                coverResId = R.drawable.cover4,
                title = "expérgo",
                artist = "NMIXX",
                info = "2023.03.20 | EP | 팝",
                trackList = listOf("Young, Dumb, Stupid", "Love Me Like This", "PAXXWORD", "Just Did It", "My Gosh", "HOME")
            )
        )

        // AlbumAdapter 설정 - onItemClick과 onPlayClick 콜백 모두 사용
        albumAdapter = AlbumAdapter(
            albums,
            onItemClick = { album ->
                navigateToAlbumFragment(album)
            },
            onPlayClick = { album ->
                // 재생 버튼 클릭 시 MiniPlayer 업데이트
                (activity as MainActivity).updateMiniPlayer(album)
            }
        )

        // RecyclerView 설정
        binding.recyclerViewAlbums.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
    }

    // 앨범 상세 화면으로 이동
    private fun navigateToAlbumFragment(album: Album) {
        val albumFragment = AlbumFragment().apply {
            arguments = Bundle().apply {
                putString("albumTitle", album.title)
                putString("artistName", album.artist)
                putInt("albumCoverResId", album.coverResId)
                putString("albumInfo", album.info)
                putStringArrayList("trackList", ArrayList(album.trackList))
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, albumFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }
}
