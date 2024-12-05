package com.example.umc_week3

interface SignUpView {
    fun onSignUpSuccess(response: SignUpResponse)
    fun onSignUpFailure(message: String)
}