package com.example.umc_week5_didi

import com.example.umc_week5_didi.R
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val textViewNote: TextView = findViewById(R.id.textViewNote)
        val note = getIntent().getStringExtra("note")
        textViewNote.text = note
    }
}
