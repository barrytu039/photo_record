package com.barrytu.photorecord.db.photorecord

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_record_table")
data class PhotoRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @ColumnInfo(name = "photoUri") val photoUri: String,
    @ColumnInfo(name = "datetime") val datetime: Long,
    @ColumnInfo(name = "price") val price: Long
)