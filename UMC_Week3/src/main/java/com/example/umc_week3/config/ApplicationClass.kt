package com.example.umc_week3.config

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "d482894fe155a2bb8721ea71eba197a1")
    }

}