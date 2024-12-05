package com.example.umc_week3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: Like)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    suspend fun deleteLike(userId: Int, albumId: Int)

    @Query("SELECT * FROM LikeTable WHERE userId = :userId")
    suspend fun getLikesByUser(userId: Int): List<Like>

    @Query("SELECT EXISTS(SELECT 1 FROM LikeTable WHERE userId = :userId AND albumId = :albumId)")
    suspend fun isAlbumLikedByUser(userId: Int, albumId: Int): Boolean
}