package com.mingolab.myapplication.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mingolab.myapplication.MyApplication
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.repository.localDB.DayPhotoDao
import com.mingolab.myapplication.repository.localDB.DayPhotoDatabase
import com.mingolab.myapplication.repository.services.LocalDBService
import com.mingolab.myapplication.repository.services.NasaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PhotoViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private lateinit var context : Context

    private lateinit var db: DayPhotoDatabase
    private lateinit var localDBService: LocalDBService
    private lateinit var nasaService: NasaService

    init{
        context = MyApplication.context()
        localDBService = LocalDBService()
        db = localDBService.getInstance(context)
        nasaService = NasaService()
        nasaService.getTodayPhotos(db.DayPhotoDao())
    }


    fun checkLatestPhotos(){
        Log.d(TAG, "check Lastest Photo")
    }

    var dbDao = db?.DayPhotoDao()

    var photoList = dbDao!!.getAll()

    var curPhoto= dbDao?.getLatestPhoto()

    fun setPhoto(photo: DayPhoto){
        curPhoto = photo
    }

    fun getPhoto(): DayPhoto? = curPhoto

}