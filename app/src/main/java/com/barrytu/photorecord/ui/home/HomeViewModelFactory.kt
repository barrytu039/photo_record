package com.barrytu.photorecord.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.barrytu.photorecord.db.photorecord.PhotoRecordRepository
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(val application: Application, val photoRecordRepository: PhotoRecordRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class, ClassCastException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(application, photoRecordRepository) as T
    }

}