package com.barrytu.photorecord.db.photorecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotoRecordEntity::class], version = 1, exportSchema = false)
abstract class PhotoRecordRoomDatabase: RoomDatabase(){

    abstract fun photoRecordDao(): PhotoRecordDao

    companion object {
        private var INSTANCE: PhotoRecordRoomDatabase? = null
        fun getDataBase(context: Context) : PhotoRecordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRecordRoomDatabase::class.java,
                    "photo_record_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}