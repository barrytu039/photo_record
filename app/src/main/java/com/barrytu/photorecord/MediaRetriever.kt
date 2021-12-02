package com.barrytu.mediastoreretriever

import androidx.lifecycle.MutableLiveData
import com.barrytu.photorecord.PhotoRecordApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MediaRetriever {

    private var photoRetriever : PhotoRetriever = PhotoRetriever(PhotoRecordApplication.getAppContext().contentResolver)

    private var videoRetriever : VideoRetriever

    val mediaMutableLiveData : MutableLiveData<List<MediaEntity>> = MutableLiveData()

    init {
        videoRetriever = VideoRetriever(PhotoRecordApplication.getAppContext().contentResolver)
    }

    suspend fun scanMediaItem() = withContext(Dispatchers.IO) {
        val photoUris = async { photoRetriever.scanItem() }
        val videoUris = async { videoRetriever.scanItem() }
        val appMediaUris = async { photoRetriever.scanAppDir() }
        val mediaUris = mutableListOf<MediaEntity>()
        mediaUris.addAll(appMediaUris.await())
        mediaUris.addAll(photoUris.await())
        mediaUris.addAll(videoUris.await())
        mediaMutableLiveData.postValue(mediaUris)
    }

    suspend fun scanSpecificItem() = withContext(Dispatchers.IO) {

    }

}