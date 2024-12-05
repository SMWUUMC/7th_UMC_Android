package com.example.umc_week3

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

data class SignUpResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: SignUpResult?
)

data class SignUpResult(
    val memberId: Int,
    val createdAt: String,
    val updatedAt: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: LoginResult?
)

data class LoginResult(
    @SerializedName("memberId")
    val memberId: Int,

    @SerializedName("accessToken")
    val accessToken: String
)