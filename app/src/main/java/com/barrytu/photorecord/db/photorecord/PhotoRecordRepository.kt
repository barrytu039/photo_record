package com.barrytu.photorecord.db.photorecord

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRecordRepository @Inject constructor(private val photoRecordDao: PhotoRecordDao) {

    val allPhotoRecords: Flow<List<PhotoRecordEntity>> = photoRecordDao.getAllPhotoRecord()

    suspend fun insert(photoRecordEntity: PhotoRecordEntity) {
        photoRecordDao.insert(photoRecordEntity)
    }
}