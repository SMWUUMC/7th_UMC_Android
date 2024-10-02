package com.haeun.umc_week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val stamp1Button: ImageButton = findViewById(R.id.stamp1)
        val stamp2Button: ImageButton = findViewById(R.id.stamp2)
        val stamp3Button: ImageButton = findViewById(R.id.stamp3)
        val stamp4Button: ImageButton = findViewById(R.id.stamp4)
        val stamp5Button: ImageButton = findViewById(R.id.stamp5)

        stamp1Button.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        stamp2Button.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        stamp3Button.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        stamp4Button.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

        stamp5Button.setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }

    }
}