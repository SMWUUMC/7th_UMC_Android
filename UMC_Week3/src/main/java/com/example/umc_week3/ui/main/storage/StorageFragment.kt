package com.example.umc_week3.ui.main.storage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.umc_week3.databinding.FragmentStorageBinding
import com.example.umc_week3.ui.main.storage.album.SavedAlbumFragment
import com.example.umc_week3.ui.signin.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)

        setupLoginState()

        // 로그인/로그아웃 버튼 클릭 이벤트
        binding.lockerLoginTv.setOnClickListener {
            if (isUserLoggedIn()) {
                performLogout()
            } else {
                navigateToLogin()
            }
        }

        // ViewPager2와 TabLayout 연결
        val adapter = StoragePagerAdapter(this)
        binding.viewPager.adapter = adapter // ViewPager2에 Adapter 연결

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                2 -> "저장앨범"
                else -> null
            }
        }.attach() // TabLayout과 ViewPager2를 연결

        return binding.root
    }

    private fun setupLoginState() {
        val loggedIn = isUserLoggedIn()
        Log.d("LOGIN-SETUP", "User logged in: $loggedIn") // 로그인 상태 확인 로그

        if (loggedIn) {
            binding.lockerLoginTv.text = "로그아웃"
        } else {
            binding.lockerLoginTv.text = "로그인"
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val jwt = sharedPreferences.getString("jwt", null)
        val userId = sharedPreferences.getLong("userId", -1L)
        Log.d("LOGIN-STATE", "JWT: $jwt, UserID: $userId") // 상태 확인 로그

        return !jwt.isNullOrEmpty() && userId > 0
    }

    private fun performLogout() {
        // 카카오 로그아웃 추가
        if (UserApiClient.instance != null) {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("LOGOUT", "카카오 로그아웃 실패", error)
                } else {
                    Log.d("LOGOUT", "카카오 로그아웃 성공")
                }
            }
        }
        sharedPreferences.edit()
            .clear() // 모든 로그인 관련 데이터 삭제
            .apply()

        // 저장 앨범 초기화
        refreshSavedAlbums(-1)

        Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun refreshSavedAlbums(userId: Int) {
        val savedAlbumFragment = childFragmentManager.fragments.find {
            it is SavedAlbumFragment
        } as? SavedAlbumFragment

        savedAlbumFragment?.let {
            it.loadSavedAlbums()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티 종료
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}