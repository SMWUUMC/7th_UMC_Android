package com.haeun.umc_week3
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haeun.umc_week3.databinding.FragmentBannerBinding

class BannerFragment() : Fragment() {

    lateinit var  binding : FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container ,false)

        return binding.root
    }
}