package com.example.sevenminutesworkout.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sevenminutesworkout.data.models.History

@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {

        private const val DB_NAME = "history_db"

        @Volatile
        private var INSTANCE: HistoryDatabase? = null
        private val LOCK = Any()

        fun getInstance(ctx: Context): HistoryDatabase {
            synchronized(LOCK) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        ctx,
                        HistoryDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE = instance
                return instance
            }
        }

    }

}