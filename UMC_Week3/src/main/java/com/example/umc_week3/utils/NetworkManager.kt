package com.example.umc_week3.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkManager {
    private const val BASE_URL = "http://3.35.121.185"

    // 로깅 인터셉터 생성
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 요청 및 응답 본문까지 로깅
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("HTTP-REQUEST", "URL: ${request.url}, Headers: ${request.headers}, Method: ${request.method}")
            try {
                val response = chain.proceed(request)
                Log.d("HTTP-RESPONSE", "Code: ${response.code}, Message: ${response.message}")
                response
            } catch (e: Exception) {
                Log.e("HTTP-ERROR", "Error during request: ${e.message}", e)
                throw e
            }
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}