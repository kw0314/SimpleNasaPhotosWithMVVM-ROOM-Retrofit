package com.mingolab.myapplication.repository.remoteDB

import com.mingolab.myapplication.repository.localDB.DayPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonNasaApi {
    @GET("/planetary/apod")
    suspend fun getAPOD (
        @Query("date") Date: String,
        @Query("hd") HDImage: Boolean,
        @Query("api_key") API_KEY: String): DayPhoto

    @GET("/planetary/apod")
    suspend fun getAPODS(
        @Query("start_date") StartDate: String,
        @Query("end_date") EndDate: String,
        @Query("hd") HDImage: Boolean,
        @Query("api_key") API_KEY: String): List<DayPhoto>
}