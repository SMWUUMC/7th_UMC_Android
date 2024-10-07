package com.example.umc_week2

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import com.example.umc_week2.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isImageChanged = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.emojibox.setOnClickListener {
            if (!isImageChanged) {
                binding.emojibox.setImageResource(R.drawable.emoji)
                val fadeIn = AlphaAnimation(0f, 1f).apply {
                    duration = 1000 // 1초 동안 페이드 인 효과
                    fillAfter = true // 애니메이션 후 상태를 유지
                }
                binding.emojibox.startAnimation(fadeIn)
                isImageChanged = true // 한 번 클릭되면 더 이상 동작하지 않도록 설정
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
