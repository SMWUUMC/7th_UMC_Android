package com.example.umc_week3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_week3.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDatabase: SongDatabase
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

        // Room Database 초기화
        userDatabase = SongDatabase.getDatabase(this)

        // 이메일 도메인 선택
        binding.loginEmailListIv.setOnClickListener {
            showEmailDomainMenu()
        }

        // 비밀번호 표시/숨김
        binding.loginHidePasswordIv.setOnClickListener {
            togglePasswordVisibility()
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

            // 로그인 로직 수행
            performLogin(email, password)
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

        lifecycleScope.launch {
            try {
                val user = userDatabase.userDao().getUserByEmail(email)
                if (user == null) {
                    Toast.makeText(this@LoginActivity, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show()
                } else if (user.password != password) {
                    Toast.makeText(this@LoginActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val fakeJwt = "jwt_for_user_${user.id}" // 서버에서 JWT를 발급받았다고 가정
                    saveLoginState(fakeJwt, user.id)
                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

                    // 저장 앨범 데이터 초기화
                    refreshSavedAlbums(user.id)

                    navigateToMainActivity()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "로그인 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshSavedAlbums(userId: Int) {
        val sharedPreferences = getSharedPreferences("UMC_PREFS", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putInt("currentUserId", userId)
            .apply()
    }

    private fun saveLoginState(jwt: String, userId: Int) {
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", true)
            .putString("jwt", jwt)
            .putInt("userId", userId)
            .apply()
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}