package com.mingolab.myapplication.repository.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photoTable")
data class DayPhoto (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

    var copyRightval: String = "",
    var date: String = "",
    var explanation: String = "",
    var hdUrl: String = "",
    var mediaType: String = "",
    var serviceVersion: String = "",
    var title: String = "",
    var url: String = ""
)