package com.haeun.umc_week2

import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haeun.umc_week2.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    private lateinit var mBinding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditBinding.inflate(inflater, container, false)

        return mBinding.root
    }
}

// 나머지 EditFragment, CalendarFragment, ProfileFragment도 동일한 방식으로 구현