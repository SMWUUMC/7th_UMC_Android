package com.example.umc_week3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Album::class], version = 2, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

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