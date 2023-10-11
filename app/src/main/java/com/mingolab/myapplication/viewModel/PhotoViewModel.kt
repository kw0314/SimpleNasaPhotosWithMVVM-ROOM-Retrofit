package com.mingolab.myapplication.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mingolab.myapplication.MyApplication
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.repository.localDB.DayPhotoDao
import com.mingolab.myapplication.repository.localDB.DayPhotoDatabase
import com.mingolab.myapplication.repository.services.LocalDBService
import com.mingolab.myapplication.repository.services.NasaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PhotoViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var context : Context = MyApplication.context()

    private var db: DayPhotoDatabase
    private var localDBService: LocalDBService
    private var nasaService: NasaService
    private var dbDao: DayPhotoDao

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init{
        localDBService = LocalDBService()
        db = localDBService.getInstance(context)
        dbDao = db.DayPhotoDao()
        nasaService = NasaService()
        checkLastPhotos()
        _isLoading.value = false
    }

    fun getContext() = context

    fun checkLastPhotos(){
        _isLoading.value = true

        var lastPhoto = dbDao.getLatestPhoto()

        if (lastPhoto==null) nasaService.initializedPhoto(db.DayPhotoDao())
        else nasaService.getLatestPhotos(db.DayPhotoDao(), lastPhoto.date)
        _isLoading.value = false
    }

    fun checkPastPhotos(){
        _isLoading.value = true
        var oldPhoto = dbDao.getOldestPhoto()
        nasaService.getOldPhotos(db.DayPhotoDao(), oldPhoto.date)
        _isLoading.value = false
    }

    val photoList = dbDao!!.getAll()



//    var photoList = dbDao!!.getAll()

    var curPhoto= dbDao?.getLatestPhoto()

    fun setPhoto(photo: DayPhoto){
        curPhoto = photo
    }

    fun getPhoto(): DayPhoto? = curPhoto

}