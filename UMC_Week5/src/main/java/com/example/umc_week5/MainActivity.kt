package com.example.umc_week5

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memoText: String? = null // 메모 내용을 저장하는 전역 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 확인 화면으로 이동
        binding.nextButton.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java).apply {
                putExtra("memoText", binding.memoEditText.text.toString())
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.memoEditText.setText(memoText ?: "")
    }

    override fun onPause() {
        super.onPause()
        memoText = binding.memoEditText.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        // 메모 다시 작성 여부를 묻는 다이얼로그
        AlertDialog.Builder(this).apply {
            setTitle("메모 다시 작성")
            setMessage("이전에 작성하던 내용을 지우고 새로 작성하시겠습니까?")
            setPositiveButton("예") { _, _ ->
                memoText = null // 이전 메모 내용을 지우기
                binding.memoEditText.setText("")
            }
            setNegativeButton("아니오", null)
            show()
        }
    }
}
