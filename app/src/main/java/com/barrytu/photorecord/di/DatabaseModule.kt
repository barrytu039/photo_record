package com.barrytu.photorecord.di

import android.content.Context
import androidx.room.Room
import com.barrytu.photorecord.db.photorecord.PhotoRecordDao
import com.barrytu.photorecord.db.photorecord.PhotoRecordRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providePhotoRecordDao(photoRecordRoomDatabase: PhotoRecordRoomDatabase): PhotoRecordDao {
        return photoRecordRoomDatabase.photoRecordDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PhotoRecordRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            PhotoRecordRoomDatabase::class.java,
            "photo_record_database"
        ).build()
    }
}