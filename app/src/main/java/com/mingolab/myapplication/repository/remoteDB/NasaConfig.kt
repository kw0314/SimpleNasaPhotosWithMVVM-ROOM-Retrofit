package com.mingolab.myapplication.repository.remoteDB

import java.time.LocalDate
import java.time.ZoneId

class NasaConfig {
    private var TIME_ZONE = "PST"
    private val URL ="https://api.nasa.gov"
    private val API_KEY = "T6ftCnk5UaPjbON08U8ZvhuXTfbrcgUiikeFPEjk"
    private val _date= LocalDate.now(ZoneId.of(TIME_ZONE))
    private var startDate:String=(_date.minusDays(14)).toString()
    private var endDate:String=_date.toString()
    private var lastDate:String=startDate

    private var hd:Boolean = true

    public fun getURL():String {return URL}
    public fun getApiKey():String {return API_KEY}

    public fun getStartDate():String {return startDate}
    public fun getEndDate():String {return endDate}
    public fun getLastDate():String {return lastDate}
    public fun getHd():Boolean {return hd}


    public fun setStartDate(date:String){ this.startDate=date}
    public fun setEndDate(date:String){ this.endDate=date}
    public fun setLastDate(date:String){ this.lastDate=date}
    public fun setHd(value:Boolean){hd=value}

}