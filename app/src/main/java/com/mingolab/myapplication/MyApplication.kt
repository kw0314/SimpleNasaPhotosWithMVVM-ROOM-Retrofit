package com.mingolab.myapplication

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.mingolab.myapplication.repository.services.LocalDBService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {

    val isDark = mutableStateOf(false)
//    var INSTANCE = this.applicationContext


    fun toggleTheme() {
        isDark.value = !isDark.value
    }

    init{
        instance = this
    }

    companion object {
        var instance: MyApplication? = null
        fun context() : Context {
            return instance!!.applicationContext
        }
    }

//    override fun onCreate() {
//        super.onCreate()
//        LocalDBService.startDB(this)
//    }
}