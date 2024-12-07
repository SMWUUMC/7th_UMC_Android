package com.example.umc_week3.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithSongs(
    @Embedded val album: Album,

    @Relation(
        parentColumn = "id",      // Album의 기본 키
        entityColumn = "albumIdx" // Song 테이블에서 Album을 참조하는 외래 키
    )
    val songs: List<Song>
)