package com.mingolab.myapplication.repository.remoteDB

import java.time.LocalDate
import java.time.ZoneId

class NasaConfig {
    // for API connection
    private val URL ="https://api.nasa.gov"
    private val API_KEY = "T6ftCnk5UaPjbON08U8ZvhuXTfbrcgUiikeFPEjk"
    private var hd:Boolean = true

    public fun getURL():String {return URL}
    public fun getApiKey():String {return API_KEY}
    public fun getHd(): Boolean {return hd}


    // for date handling
    private var TIME_ZONE = "PST"
    private val _date= LocalDate.now(ZoneId.of(TIME_ZONE))
    private var today: String = _date.toString()
    private lateinit var oldDate: String
    private lateinit var upToDate: String


    public fun getToday():String {return today}
    public fun getOldDate():String {return oldDate}
    public fun getUpToDate():String {return upToDate}

    public fun setOldDate(date:String){ this.oldDate=date}
    public fun setUpToDate(date:String){ this.upToDate=date}

}