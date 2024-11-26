package com.example.umc_week3

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Album")
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val singer: String = "",
    val releaseDate: String = "",
    val type: String = "",
    val genre: String = "",
    val coverImg: Int? = null
) {
    @Ignore
    constructor(title: String, singer: String) : this(
        id = 0,
        title = title,
        singer = singer
    )
}