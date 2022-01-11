package com.barrytu.photorecord

import android.app.Application
import android.content.Context
import com.barrytu.photorecord.tools.MediaRetriever
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
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        _mediaRetriever = MediaRetriever()
    }

}