package com.example.umc_week3

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Headers

interface AuthRetrofitInterface {

    @Headers("Content-Type: application/json") // Content-Type 명시
    @POST("/join")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @Headers("Content-Type: application/json") // Content-Type 명시
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}