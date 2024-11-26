package com.example.umc_week3

import BannerItem
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
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
    private lateinit var songDatabase: SongDatabase // RoomDB 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RoomDB 초기화
        songDatabase = SongDatabase.getDatabase(requireContext())

        // 배너 데이터 설정
        setupBanner()

        // 앨범 데이터 설정
        fetchAlbumData()
    }

    private fun setupBanner() {
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
    }

    private fun fetchAlbumData() {
        lifecycleScope.launch {
            val albums = songDatabase.albumDao().getAllAlbums()

            // RoomDB에서 가져온 데이터로 AlbumAdapter 설정
            albumAdapter = AlbumAdapter(
                albums,
                onItemClick = { album ->
                    navigateToAlbumFragment(album.id)
                },
                onPlayClick = { album ->
                    // 재생 버튼 클릭 시 MiniPlayer 업데이트
                    lifecycleScope.launch {
                        val firstSong = songDatabase.songDao().getSongsByAlbumId(album.id).firstOrNull()
                        if (firstSong != null) {
                            (activity as? MainActivity)?.updateMiniPlayerWithSong(firstSong)
                        } else {
                            Toast.makeText(requireContext(), "앨범에 곡이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            // RecyclerView 설정
            binding.recyclerViewAlbums.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = albumAdapter
            }
        }
    }

    private fun navigateToAlbumFragment(albumId: Int) {
        val albumFragment = AlbumFragment.newInstance(albumId) // 수정된 AlbumFragment와 연동
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