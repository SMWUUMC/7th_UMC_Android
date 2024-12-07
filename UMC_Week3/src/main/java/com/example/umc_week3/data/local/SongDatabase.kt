package com.example.umc_week3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_week3.data.entities.Album
import com.example.umc_week3.data.entities.Like
import com.example.umc_week3.data.entities.Song
import com.example.umc_week3.data.entities.User

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 4, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun userDao(): UserDao
    abstract fun likeDao(): LikeDao // LikeDao 추가

    companion object {
        @Volatile
        private var instance: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    .fallbackToDestructiveMigration() // 기존 데이터베이스 삭제 후 새로 생성
                    .build()
                    .also { instance = it }
            }
        }
    }
}