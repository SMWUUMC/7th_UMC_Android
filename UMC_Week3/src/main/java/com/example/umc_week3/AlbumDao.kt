package com.example.umc_week3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long

    @Query("SELECT * FROM Album WHERE id = :albumId")
    suspend fun getAlbumById(albumId: Int): Album?

    @Query("SELECT * FROM Album")
    suspend fun getAllAlbums(): List<Album>

    @Query("SELECT coverImg FROM Album WHERE id = :albumIdx")
    suspend fun getAlbumCoverById(albumIdx: Long): Int?

    // 앨범 ID로 앨범과 곡 목록 가져오기
    @Transaction
    @Query("SELECT * FROM Album WHERE id = :albumId")
    suspend fun getAlbumWithSongs(albumId: Int): AlbumWithSongs?
}

