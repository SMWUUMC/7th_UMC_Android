package com.haeun.umc_week5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextMemo: EditText
    private var savedMemo: String? = null // 메모 내용을 저장 변수

    // onCreate : Layout XML 파일을 Activity에서 ContentView로 사용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 화면 설정

        editTextMemo = findViewById(R.id.editTextMemo)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val intent = Intent(this, DisplayActivity::class.java)
            intent.putExtra("memo", editTextMemo.text.toString())
            startActivity(intent)
        }
    }

    // onResume : onPause에서 저장한 전역변수 내용으로 EditText 내용으로 설정
    override fun onResume() {
        super.onResume()
        savedMemo?.let {
            editTextMemo.setText(it) // onPause에서 저장한 내용이 있다면 복원
        }
    }

    // onPause : 현재까지 작성한 내용 Activity의 전역변수에 담기
    override fun onPause() {
        super.onPause()
        savedMemo = editTextMemo.text.toString()
    }
    // onRestart : Dialog를 활용하여 다시 작성할거냐고 묻는 창 띄우기
    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setMessage("다시 작성하시겠습니까?")
            .setPositiveButton("네") { _, _ -> /* 아무 작업도 안 함 */ }
            .setNegativeButton("아니요") { _, _ -> savedMemo = null } // 저장했던 변수 비움
            .show()
    }
}
