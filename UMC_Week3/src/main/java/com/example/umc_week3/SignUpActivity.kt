// SignUpActivity.kt
package com.example.umc_week3

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_week3.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userDatabase: SongDatabase
    private var isPasswordVisible = false // 비밀번호 표시 여부
    private var isPasswordCheckVisible = false // 비밀번호 확인 표시 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DB 초기화
        userDatabase = SongDatabase.getDatabase(this)

        // 가입 완료 버튼 클릭 리스너
        binding.signUpSignUpBtn.setOnClickListener {
            performSignUp()
        }

        // 비밀번호 필드 토글
        binding.signUpHidePasswordIv.setOnClickListener {
            togglePasswordVisibility(
                binding.signUpPasswordEt,
                binding.signUpHidePasswordIv
            ) { isPasswordVisible = !isPasswordVisible; isPasswordVisible }
        }

        // 비밀번호 확인 필드 토글
        binding.signUpHidePasswordCheckIv.setOnClickListener {
            togglePasswordVisibility(
                binding.signUpPasswordCheckEt,
                binding.signUpHidePasswordCheckIv
            ) { isPasswordCheckVisible = !isPasswordCheckVisible; isPasswordCheckVisible }
        }

        // 비밀번호 확인 유효성 검사
        binding.signUpPasswordCheckEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePasswordMatch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 이메일 도메인 선택 드롭다운 클릭 리스너
        binding.signUpEmailListIv.setOnClickListener {
            showEmailDomainMenu()
        }
    }

    private fun showEmailDomainMenu() {
        val popupMenu = PopupMenu(this, binding.signUpEmailListIv)
        popupMenu.menu.add("gmail.com")
        popupMenu.menu.add("naver.com")
        popupMenu.menu.add("kakao.com")

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            binding.signUpDirectInputEt.setText(menuItem.title) // 선택된 도메인을 입력란에 설정
            true
        }
        popupMenu.show()
    }

    private fun performSignUp() {
        val email = binding.signUpIdEt.text.toString().trim() + "@" + binding.signUpDirectInputEt.text.toString().trim()
        val name = binding.signUpNameEt.text.toString().trim()
        val password = binding.signUpPasswordEt.text.toString().trim()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString().trim()

        // 입력값 유효성 검사
        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // ProgressBar 표시
        binding.signUpLoadingPb.visibility = View.VISIBLE

        // DB에 사용자 데이터 추가
        lifecycleScope.launch {
            val user = User(email = email, password = password, name = name)
            try {
                userDatabase.userDao().insertUser(user)

                // 회원가입 성공 후 로그인 화면으로 이동
                Toast.makeText(this@SignUpActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish() // 현재 회원가입 화면 종료

            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity, "회원가입 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            } finally {
                // ProgressBar 숨기기
                binding.signUpLoadingPb.visibility = View.GONE
            }
        }
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        toggleIcon: ImageView,
        stateProvider: () -> Boolean
    ) {
        val isVisible = stateProvider()
        if (isVisible) {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleIcon.setImageResource(R.drawable.btn_input_password) // 보이는 아이콘
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleIcon.setImageResource(R.drawable.btn_input_password_off) // 숨긴 아이콘
        }
        editText.setSelection(editText.text.length) // 커서 위치 유지
    }

    private fun validatePasswordMatch() {
        val password = binding.signUpPasswordEt.text.toString().trim()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString().trim()

        if (password != passwordCheck) {
            binding.signUpPasswordCheckEt.error = "비밀번호가 일치하지 않습니다."
        }
    }
}