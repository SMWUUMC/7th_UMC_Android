package com.example.umc_week6

data class Pokemon(
    val pokemonNumber: String,
    val name: String,
    val imageResId: Int,
    val backgroundColor: String,
    val type: String,
    val ovalColor: String,
    val weight: Double,
    val height: Double,
    val baseStats: List<Int>  // 스탯을 List로 관리
)


