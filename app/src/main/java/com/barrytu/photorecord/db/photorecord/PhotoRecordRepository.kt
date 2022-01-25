package com.barrytu.photorecord.db.photorecord

import kotlinx.coroutines.flow.Flow


class PhotoRecordRepository(private val photoRecordDao: PhotoRecordDao) {

    val allPhotoRecords: Flow<List<PhotoRecordEntity>> = photoRecordDao.getAllPhotoRecord()

    suspend fun insert(photoRecordEntity: PhotoRecordEntity) {
        photoRecordDao.insert(photoRecordEntity)
    }
}