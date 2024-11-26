package com.example.umc_week3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Query("SELECT * FROM Song WHERE id = :songId")
    suspend fun getSongById(songId: Int): Song?

    @Query("SELECT * FROM Song WHERE isLike = 1")
    suspend fun getLikedSongs(): List<Song>

    @Query("SELECT * FROM Song")
    suspend fun getAllSongs(): List<Song>

    @Query("UPDATE Song SET isLike = :isLike WHERE id = :songId")
    suspend fun updateIsLikeById(isLike: Boolean, songId: Int)

    @Update
    suspend fun updateSong(song: Song)

    @Query("SELECT * FROM Song WHERE albumIdx = :albumId")
    suspend fun getSongsByAlbumId(albumId: Int): List<Song>

    @Query("UPDATE Song SET isPlaying = :isPlaying WHERE id = :songId")
    suspend fun updateIsPlayingById(songId: Int, isPlaying: Boolean)

    @Query("UPDATE Song SET second = :second WHERE id = :songId")
    suspend fun updateSecondById(songId: Int, second: Int)

    @Query("UPDATE Song SET second = :second, isPlaying = :isPlaying WHERE id = :songId")
    suspend fun updateSongState(songId: Int, second: Int, isPlaying: Boolean)

    // 곡 ID로 앨범 정보 가져오기
    @Query("""
    SELECT * FROM Album
    WHERE id = (SELECT albumIdx FROM Song WHERE id = :songId)
""")
    suspend fun getAlbumBySongId(songId: Int): Album?
}