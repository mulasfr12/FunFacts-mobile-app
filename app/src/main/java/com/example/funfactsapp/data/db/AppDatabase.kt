package com.example.funfactsapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Fact::class, FavoriteFact::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun factDao(): FactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fun_facts_db"
                )
                    .fallbackToDestructiveMigration() // ✅ Forces Room to rebuild schema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
