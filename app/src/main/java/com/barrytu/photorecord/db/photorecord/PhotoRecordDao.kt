package com.barrytu.photorecord.db.photorecord

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoRecordDao {

    @Query("SELECT * FROM photo_record_table ORDER BY datetime ASC")
    fun getAllPhotoRecord(): Flow<List<PhotoRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: PhotoRecordEntity)

    @Query("DELETE FROM photo_record_table")
    suspend fun deleteAll()
}