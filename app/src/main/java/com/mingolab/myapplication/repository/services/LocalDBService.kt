package com.mingolab.myapplication.repository.services

import android.content.Context
import androidx.room.Room
import com.mingolab.myapplication.MyApplication
import com.mingolab.myapplication.repository.localDB.DayPhotoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class LocalDBService {

    companion object {
        private lateinit var INSTANCE: DayPhotoDatabase
        private var context: Context = MyApplication.context()


        init {

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DayPhotoDatabase::class.java,
                "dayphoto.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

        }

    }

    //        fun startDB(_context: Context) = CoroutineScope(Dispatchers.IO).async{
    private fun startDB(_context: Context) = CoroutineScope(Dispatchers.Main).async {
        synchronized(DayPhotoDatabase::class)
        {
            INSTANCE = Room.databaseBuilder(
                _context.applicationContext,
                DayPhotoDatabase::class.java,
                "dayphoto.db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
        return@async INSTANCE
    }


    fun getInstance(_context: Context): DayPhotoDatabase {
        return if (INSTANCE == null) {
            startDB(_context)
            INSTANCE
        } else {
            INSTANCE
        }
    }

    fun destoryInstance() {
        INSTANCE.close()
    }

}
