package com.example.umc_week3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Song",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumIdx"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["albumIdx"])]
)
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String = "", // 곡 제목
    var singer: String = "", // 가수명
    var second: Int = 0, //현재 재생 시간
    var playtime: Int = 0, // 전체 재생 시간
    var isPlaying: Boolean = false, //재생 상태 여부
    var music: Int = 0, //mp3
    var isLike: Boolean = false, //좋아요 여부
    val albumIdx: Long = 0 //앨범 인덱스
)