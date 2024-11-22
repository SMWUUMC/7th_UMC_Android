package com.example.umc_week6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.umc_week6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 설정
        val pokemonList = listOf(
            Pokemon(
                pokemonNumber = "#001", name = "Bulbasaur", imageResId = R.drawable.bulbasaur, backgroundColor = "#C4E6D7",
                type = "grass", ovalColor = "#4DB051", weight = 6.9, height = 0.7, baseStats = listOf(199, 166, 164, 130, 619)
            ),
            Pokemon(
                pokemonNumber = "#004", name = "Charmander", imageResId = R.drawable.charmander, backgroundColor = "#FFA726",
                type = "fire", ovalColor = "#F4511E", weight = 8.5, height = 0.6, baseStats = listOf(180, 158, 140, 200, 390)
            ),
            Pokemon(
                pokemonNumber = "#007", name = "Squirtle", imageResId = R.drawable.squirtle, backgroundColor = "#b0d8e2",
                type = "water", ovalColor = "#3093fb", weight = 9.0, height = 0.5, baseStats = listOf(110, 110, 170, 168, 511)
            ),
            Pokemon(
                pokemonNumber = "#819", name = "Skwovet", imageResId = R.drawable.skwovet, backgroundColor = "#c3b7ad",
                type = "normal", ovalColor = "#999999", weight = 2.5, height = 0.3, baseStats = listOf(250, 208, 214, 96, 985)
            ),Pokemon(
                pokemonNumber = "#001", name = "Bulbasaur", imageResId = R.drawable.bulbasaur, backgroundColor = "#C4E6D7",
                type = "grass", ovalColor = "#4DB051", weight = 6.9, height = 0.7, baseStats = listOf(199, 166, 164, 130, 619)
            ),
            Pokemon(
                pokemonNumber = "#004", name = "Charmander", imageResId = R.drawable.charmander, backgroundColor = "#FFA726",
                type = "fire", ovalColor = "#F4511E", weight = 8.5, height = 0.6, baseStats = listOf(180, 158, 140, 200, 390)
            ),
            Pokemon(
                pokemonNumber = "#007", name = "Squirtle", imageResId = R.drawable.squirtle, backgroundColor = "#b0d8e2",
                type = "water", ovalColor = "#3093fb", weight = 9.0, height = 0.5, baseStats = listOf(110, 110, 170, 168, 511)
            ),
            Pokemon(
                pokemonNumber = "#819", name = "Skwovet", imageResId = R.drawable.skwovet, backgroundColor = "#c3b7ad",
                type = "normal", ovalColor = "#999999", weight = 2.5, height = 0.3, baseStats = listOf(250, 208, 214, 96, 985)
            ),Pokemon(
                pokemonNumber = "#001", name = "Bulbasaur", imageResId = R.drawable.bulbasaur, backgroundColor = "#C4E6D7",
                type = "grass", ovalColor = "#4DB051", weight = 6.9, height = 0.7, baseStats = listOf(199, 166, 164, 130, 619)
            ),
            Pokemon(
                pokemonNumber = "#004", name = "Charmander", imageResId = R.drawable.charmander, backgroundColor = "#FFA726",
                type = "fire", ovalColor = "#F4511E", weight = 8.5, height = 0.6, baseStats = listOf(180, 158, 140, 200, 390)
            ),
            Pokemon(
                pokemonNumber = "#007", name = "Squirtle", imageResId = R.drawable.squirtle, backgroundColor = "#b0d8e2",
                type = "water", ovalColor = "#3093fb", weight = 9.0, height = 0.5, baseStats = listOf(110, 110, 170, 168, 511)
            ),
            Pokemon(
                pokemonNumber = "#819", name = "Skwovet", imageResId = R.drawable.skwovet, backgroundColor = "#c3b7ad",
                type = "normal", ovalColor = "#999999", weight = 2.5, height = 0.3, baseStats = listOf(250, 208, 214, 96, 985)
            )
        )

        // RecyclerView 설정
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = PokemonAdapter(this, pokemonList)
    }
}
