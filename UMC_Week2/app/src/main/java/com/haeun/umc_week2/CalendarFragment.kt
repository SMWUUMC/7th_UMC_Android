package com.haeun.umc_week2

import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haeun.umc_week2.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    private lateinit var mBinding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCalendarBinding.inflate(inflater, container, false)

        return mBinding.root
    }
}