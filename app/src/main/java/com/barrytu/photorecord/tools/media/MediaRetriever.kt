package com.barrytu.photorecord.tools.media

import androidx.lifecycle.MutableLiveData
import com.barrytu.mediastoreretriever.MediaEntity
import com.barrytu.photorecord.PhotoRecordApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class MediaRetriever {

    private var photoRetriever : PhotoRetriever = PhotoRetriever(PhotoRecordApplication.getAppContext().contentResolver)

    private var videoRetriever : VideoRetriever = VideoRetriever(PhotoRecordApplication.getAppContext().contentResolver)

    val mediaMutableLiveData : MutableLiveData<List<MediaEntity>> = MutableLiveData()

    suspend fun scanMediaItem() = withContext(Dispatchers.IO) {
        val photoUris = async { photoRetriever.scanItem() }
        val videoUris = async { videoRetriever.scanItem() }
//        val appMediaUris = async { photoRetriever.scanAppDir() }
        val mediaUris = mutableListOf<MediaEntity>()
//        mediaUris.addAll(appMediaUris.await())
        mediaUris.addAll(photoUris.await())
        mediaUris.addAll(videoUris.await())
        mediaMutableLiveData.postValue(mediaUris)
    }

}