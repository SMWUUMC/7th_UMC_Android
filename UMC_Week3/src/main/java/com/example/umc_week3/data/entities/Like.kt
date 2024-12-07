package com.example.umc_week3.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Like(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Long, // 현재 로그인한 사용자 ID
    val albumId: Int // 좋아요한 앨범 ID
)