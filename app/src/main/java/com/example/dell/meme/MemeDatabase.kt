package com.example.dell.meme

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemeTable::class], version = 1, exportSchema = false)
abstract class MemeDatabase : RoomDatabase() {

    abstract fun getMemeTableDao(): MemeTableDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MemeDatabase? = null

        fun getDatabase(context: Context): MemeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemeDatabase::class.java,
                        "meme_table"
                ).build()
                INSTANCE = instance
//                return instance
                instance
            }
        }

    }

}