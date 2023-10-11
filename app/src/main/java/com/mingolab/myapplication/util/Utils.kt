package com.mingolab.myapplication.util

import java.time.LocalDate


class Utils{
    fun toDate(dateString: String): LocalDate {
        var year=dateString.substring(0,4).toInt()
        var month=dateString.substring(5,7).toInt()
        var dayofMonth=dateString.substring(8,10).toInt()
        return LocalDate.of(year,month,dayofMonth)
    }
}
