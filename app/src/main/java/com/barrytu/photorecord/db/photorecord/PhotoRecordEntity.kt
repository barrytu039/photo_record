package com.barrytu.photorecord.db.photorecord

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_record_table")
data class PhotoRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "photoUri") var photoUri: String? = null,
    @ColumnInfo(name = "datetime") var datetime: Long? = null,
    @ColumnInfo(name = "cost") var cost: Long? = null) {

    fun isRecordCompleted() : Boolean {
        return !photoUri.isNullOrEmpty() && cost != null
    }
}