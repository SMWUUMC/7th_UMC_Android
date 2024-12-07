package com.example.umc_week3.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week3.R
import com.example.umc_week3.data.remote.AuthService
import com.example.umc_week3.data.remote.LoginRequest
import com.example.umc_week3.data.remote.LoginView
import com.example.umc_week3.databinding.ActivityLoginBinding
import com.example.umc_week3.ui.main.MainActivity
import com.example.umc_week3.ui.signup.SignUpActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService
    private var isPasswordVisible = false // 비밀번호 표시 여부
    private val sharedPreferences by lazy { getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 상태 확인
        if (isUserLoggedIn()) {
            navigateToMainActivity()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AuthService 초기화
        authService = AuthService()
        authService.setLoginView(this)

        // 이메일 도메인 선택
        binding.loginEmailListIv.setOnClickListener {
            showEmailDomainMenu()
        }

        // 비밀번호 표시/숨김
        binding.loginHidePasswordIv.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.loginKakakoLoginIv.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("KAKAO-LOGIN", "카카오톡 로그인 실패", error)

                    // 카카오톡 로그인 실패 시 카카오계정 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                        if (error != null) {
                            Log.e("KAKAO-LOGIN", "카카오계정 로그인 실패", error)
                        } else if (token != null) {
                            Log.i("KAKAO-LOGIN", "카카오계정 로그인 성공 ${token.accessToken}")
                            fetchUserInfoAndSave(token)
                        }
                    }
                } else if (token != null) {
                    Log.i("KAKAO-LOGIN", "카카오톡 로그인 성공 ${token.accessToken}")
                    fetchUserInfoAndSave(token)
                }
            }
        }

        // 회원가입 화면으로 이동
        binding.loginSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭 이벤트
        binding.loginSignInBtn.setOnClickListener {
            val email = binding.loginIdEt.text.toString().trim() + "@" + binding.loginDirectInputEt.text.toString().trim()
            val password = binding.loginPasswordEt.text.toString().trim()

            performLogin(email, password)
        }
    }

    private fun fetchUserInfoAndSave(token: OAuthToken) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KAKAO-USER", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val jwt = token.accessToken
                val userId = user.id ?: -1L
                val nickname = user.kakaoAccount?.profile?.nickname ?: "알 수 없는 사용자"
                val email = user.kakaoAccount?.email ?: "이메일 정보 없음"

                // 로그에 사용자 정보 출력
                Log.i("KAKAO-USER", "사용자 정보: $nickname, 이메일: $email, ID: $userId")

                // 사용자 정보 토스트 메시지로 출력
                Toast.makeText(this, "닉네임: $nickname\n이메일: $email", Toast.LENGTH_LONG).show()

                saveLoginState(jwt, userId)
                navigateToMainActivity()
            }
        }
    }

    private fun showEmailDomainMenu() {
        val popupMenu = PopupMenu(this, binding.loginEmailListIv)
        popupMenu.menu.add("gmail.com")
        popupMenu.menu.add("naver.com")
        popupMenu.menu.add("kakao.com")

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            binding.loginDirectInputEt.setText(menuItem.title)
            true
        }
        popupMenu.show()
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            // 비밀번호 보이기
            binding.loginPasswordEt.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password) // 보이는 아이콘
        } else {
            // 비밀번호 숨기기
            binding.loginPasswordEt.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password_off) // 숨긴 아이콘
        }
        // 커서 위치 유지
        binding.loginPasswordEt.setSelection(binding.loginPasswordEt.text.length)
    }

    private fun performLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("LOGIN-ACTIVITY", "User Input: email=$email, password=$password") // 입력 데이터 로그

        val request = LoginRequest(email, password)
        authService.login(request)
    }

    private fun saveLoginState(jwt: String, userId: Long) {
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", true)
            .putString("jwt", jwt) // JWT 저장
            .putLong("userId", userId) // User ID를 Long으로 저장
            .apply()
        Log.d("LOGIN-SAVE", "JWT: $jwt, UserID: $userId") // 저장 확인 로그
    }

    private fun isUserLoggedIn(): Boolean {
        val jwt = sharedPreferences.getString("jwt", null)
        val userId = sharedPreferences.getLong("userId", -1L)

        Log.d("LOGIN-STATE", "JWT: $jwt, UserID: $userId") // 상태 확인 로그

        return !jwt.isNullOrEmpty() && userId != -1L
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }

    // LoginView 인터페이스 구현
    override fun onLoginSuccess(token: String, userId: Long) {
        saveLoginState(token, userId) // User ID를 저장
        navigateToMainActivity()
    }

    override fun onLoginFailure(message: String) {
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_SHORT).show()
    }
}