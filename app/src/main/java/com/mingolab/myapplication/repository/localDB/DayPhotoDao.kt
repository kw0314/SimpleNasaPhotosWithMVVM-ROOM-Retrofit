package com.mingolab.myapplication.repository.localDB

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DayPhotoDao {

    @Insert
    fun insert(photo:DayPhoto)

    @Insert
    fun insertAll(vararg photos:DayPhoto)

    @Query("SELECT * FROM photoTable " +
            "ORDER BY photoTable.date DESC"
    )
    fun getAll(): List<DayPhoto>

    @Query("SELECT * FROM photoTable " +
            "ORDER BY photoTable.date DESC LIMIT 1"
    )
    fun getLatestPhoto(): DayPhoto

    @Query("SELECT * FROM photoTable " +
            "ORDER BY photoTable.date ASC LIMIT 1"
    )
    fun getOldestPhoto(): DayPhoto


    @Query("SELECT count(*) FROM photoTable" +
            " WHERE photoTable.date=:date")
    fun getPhotoOfThatDay(date: String): Int

    @Delete
    fun delete(photo:DayPhoto)

    @Query("DELETE from photoTable")
    fun claerAllUsers()
}