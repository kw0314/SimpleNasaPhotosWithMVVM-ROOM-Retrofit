package com.mingolab.myapplication.repository.localDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DayPhoto::class], version = 2)
abstract class DayPhotoDatabase: RoomDatabase() {
    abstract fun DayPhotoDao(): DayPhotoDao
}