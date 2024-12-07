package com.example.umc_week3.data.remote

import android.util.Log
import com.example.umc_week3.utils.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    // SignUpView 연결
    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    // LoginView 연결
    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(request: SignUpRequest) {
        val authService = NetworkManager.retrofit.create(AuthRetrofitInterface::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("SIGNUP-REQUEST", "Sending request: $request") // 요청 데이터 로깅

                val response = authService.signUp(request)

                Log.d(
                    "SIGNUP-RESPONSE",
                    "Response: code=${response.code()}, body=${response.body()}, errorBody=${response.errorBody()?.string()}"
                ) // 응답 데이터 로깅

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess) {
                            Log.d("SIGNUP-SUCCESS", "Sign-up success: $body") // 성공 로그
                            signUpView.onSignUpSuccess(body)
                        } else {
                            Log.e("SIGNUP-FAILURE", "Sign-up failed: ${body?.message}") // 실패 로그
                            signUpView.onSignUpFailure(body?.message ?: "회원가입 실패")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("SIGNUP-ERROR", "Code: ${response.code()}, Error: $errorBody") // 서버 오류 로그

                        // 중복 이메일 에러 처리
                        if (response.code() == 400 && errorBody?.contains("AUTH_010") == true) {
                            signUpView.onSignUpFailure("이미 존재하는 이메일입니다.")
                        } else {
                            signUpView.onSignUpFailure("회원가입 실패: 서버 오류 (${response.code()})")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("SIGNUP-EXCEPTION", "Exception occurred: ${e.message}", e) // 예외 로그
                    signUpView.onSignUpFailure("회원가입 중 오류가 발생했습니다.")
                }
            }
        }
    }

    // AuthService - login 메서드
    fun login(request: LoginRequest) {
        val authService = NetworkManager.retrofit.create(AuthRetrofitInterface::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("LOGIN-REQUEST", "Sending request: $request")

                val response = authService.login(request)

                Log.d("LOGIN-RESPONSE", "Response: code=${response.code()}, body=${response.body()}, errorBody=${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    val userId = body?.result?.memberId?.toLong() ?: -1L // memberId를 Long으로 변환
                    val token = body?.result?.accessToken.orEmpty()

                    Log.d("LOGIN-RESULT", "Token: $token, UserID: $userId") // 추가된 로그

                    withContext(Dispatchers.Main) {
                        if (body != null && body.isSuccess) {
                            loginView.onLoginSuccess(token, userId)
                        } else {
                            loginView.onLoginFailure(body?.message ?: "로그인 실패")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    withContext(Dispatchers.Main) {
                        Log.e("LOGIN-ERROR", "Code: ${response.code()}, Error: $errorBody")
                        loginView.onLoginFailure("로그인 실패: 서버 오류 (${response.code()})")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LOGIN-EXCEPTION", "Exception occurred: ${e.message}", e)
                    loginView.onLoginFailure("로그인 중 오류가 발생했습니다.")
                }
            }
        }
    }
}