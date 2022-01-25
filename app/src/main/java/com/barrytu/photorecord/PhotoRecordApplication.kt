package com.barrytu.photorecord

import android.app.Application
import android.content.Context
import com.barrytu.photorecord.db.photorecord.PhotoRecordRepository
import com.barrytu.photorecord.db.photorecord.PhotoRecordRoomDatabase
import com.barrytu.photorecord.tools.media.MediaRetriever
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PhotoRecordApplication : Application() {

    companion object {
        private lateinit var appContext : Context
        private var _mediaRetriever: MediaRetriever? = null
        val mediaRetriever: MediaRetriever get() = _mediaRetriever!!
        fun getAppContext() : Context {
            return appContext
        }
        lateinit var photoRecordRepository : PhotoRecordRepository
    }

    val database by lazy { PhotoRecordRoomDatabase.getDataBase(this) }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        _mediaRetriever = MediaRetriever()
        photoRecordRepository = PhotoRecordRepository(database.photoRecordDao())
    }

}