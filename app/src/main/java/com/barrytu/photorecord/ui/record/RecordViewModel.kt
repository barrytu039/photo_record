package com.barrytu.photorecord.ui.record

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.barrytu.photorecord.BaseViewModel
import com.barrytu.photorecord.db.photorecord.PhotoRecordEntity
import com.barrytu.photorecord.db.photorecord.PhotoRecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecordViewModel(application: Application, val photoRecordRepository: PhotoRecordRepository) : BaseViewModel(application) {

    private val _record = MutableLiveData<PhotoRecordEntity>().apply {
        value = PhotoRecordEntity()
    }

    val record: LiveData<PhotoRecordEntity> = _record

    fun setPhotoRecordImageUri(uri: Uri?) {
        _record.value?.photoUri = uri?.toString()
        _record.postValue(_record.value)
    }

    fun setPhotoRecordCost(cost: String?) {
        _record.value?.cost = cost?.toLong()
        _record.postValue(_record.value)
    }

    fun setPhotoRecordDateTime() {
        _record.value?.datetime = Calendar.getInstance().timeInMillis
    }

    fun insertData() {
        setPhotoRecordDateTime()
        viewModelScope.launch(Dispatchers.IO) {
            record.value?.let { record ->
                photoRecordRepository.insert(record)
                _record.postValue(PhotoRecordEntity())
            }
        }
    }
}