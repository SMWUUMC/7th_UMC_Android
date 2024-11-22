package com.example.umc_week6

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_week6.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Intent로 전달받은 데이터 가져오기
        val pokemonNumber = intent.getStringExtra("pokemonNumber") ?: "#000"
        val name = intent.getStringExtra("name") ?: "Unknown"
        val imageResId = intent.getIntExtra("imageResId", R.drawable.bulbasaur)
        val backgroundColor = intent.getStringExtra("backgroundColor") ?: "#FFFFFF"
        val type = intent.getStringExtra("type") ?: "Unknown"
        val ovalColor = intent.getStringExtra("ovalColor") ?: "#000000"
        val weight = intent.getDoubleExtra("weight", 0.0)
        val height = intent.getDoubleExtra("height", 0.0)
        val baseStats = intent.getIntegerArrayListExtra("baseStats") ?: arrayListOf(0, 0, 0, 0, 0)


        val backgroundDrawable = GradientDrawable().apply {
            setColor(Color.parseColor(backgroundColor))
            cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 60f, 60f, 60f, 60f)
        }

        val typeDrawable = GradientDrawable().apply {
            setColor(Color.parseColor(ovalColor))
            cornerRadius = 50f
        }

        // UI에 데이터 설정
        binding.pokemonNumber.text = pokemonNumber
        binding.pokemonName.text = name
        binding.pokemonImage.setImageResource(imageResId)
        binding.pokemonType.text = type
        binding.typeView.background = typeDrawable
        binding.pokemonWeight.text = "$weight KG"
        binding.pokemonHeight.text = "$height M"
        binding.topBar.background = backgroundDrawable

        configureStat(binding.hpBar, binding.hpValue, baseStats[0])
        configureStat(binding.atkBar, binding.atkValue, baseStats[1])
        configureStat(binding.defBar, binding.defValue, baseStats[2])
        configureStat(binding.spdBar, binding.spdValue, baseStats[3])
        configureStat(binding.expBar, binding.expValue, baseStats[4])

        // backButton 클릭 시 액티비티 종료
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun configureStat(progressBar: ProgressBar, valueText: TextView, currentValue: Int) {
        progressBar.progress = currentValue
        valueText.text = "$currentValue/${progressBar.max}"

        // 진행도에 따라 TextView 위치 설정
        progressBar.post {
            val progressRatio = currentValue.toFloat() / progressBar.max.toFloat()
            val progressBarWidth = progressBar.width
            val textWidth = valueText.width

            val newX = progressBar.x + (progressBarWidth * progressRatio) - (textWidth)
            valueText.x = newX.coerceAtLeast(progressBar.x) // 최소 위치는 ProgressBar의 시작점
        }
    }

}
