package com.barrytu.photorecord.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.barrytu.photorecord.BaseViewModel
import com.barrytu.photorecord.db.photorecord.PhotoRecordRepository

class HomeViewModel(application: Application, val courseRepository: PhotoRecordRepository) : BaseViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val photoRecordList = courseRepository.allPhotoRecords

}