package com.example.umc_week3

// LoginView
interface LoginView {
    fun onLoginSuccess(token: String, userId: Int) // userId 추가
    fun onLoginFailure(message: String)
}