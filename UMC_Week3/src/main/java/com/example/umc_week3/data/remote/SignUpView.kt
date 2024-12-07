package com.example.umc_week3.data.remote

interface SignUpView {
    fun onSignUpSuccess(response: SignUpResponse)
    fun onSignUpFailure(message: String)
}