package com.example.umc_week3.data.remote

// LoginView
interface LoginView {
    fun onLoginSuccess(token: String, userId: Long) // userId 추가
    fun onLoginFailure(message: String)
}