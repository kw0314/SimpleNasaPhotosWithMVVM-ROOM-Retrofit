package com.mingolab.myapplication.repository.services

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.repository.localDB.DayPhotoDao
import com.mingolab.myapplication.repository.remoteDB.JsonNasaApi
import com.mingolab.myapplication.repository.remoteDB.NasaConfig
import com.mingolab.myapplication.util.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.ZoneId


class NasaService {

    val TIME_ZONE = "PST"
    var nasaConfig: NasaConfig = NasaConfig()
    private var API: JsonNasaApi?= null

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    fun getApi(): JsonNasaApi?{
        if (API==null){
            API = Retrofit.Builder()
                .baseUrl(nasaConfig.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(JsonNasaApi::class.java)
        }
        return API
    }

    public fun initializedPhoto(db:DayPhotoDao) {
        var endDate = nasaConfig.getToday()
        var startDate = Utils().toDate(endDate).minusDays(7).toString()
        nasaConfig.setUpToDate(endDate)
        getPhotos(db, startDate, endDate)
    }
    public fun getLatestPhotos(db:DayPhotoDao, upToDateInDB: String) {
        var startDate = upToDateInDB
        var endDate = nasaConfig.getToday()
        if (startDate<endDate){
            nasaConfig.setUpToDate(endDate)
            getPhotos(db, startDate, endDate)
        } else {
            Log.d(TAG, "The DB is up-to date!! No update now.")
        }

    }


    public fun getOldPhotos(db:DayPhotoDao, oldDateInDB: String) {
        var startDate = Utils().toDate(oldDateInDB).minusDays(7).toString()
        var endDate = oldDateInDB
        nasaConfig.setOldDate(startDate)
        getPhotos(db, startDate, endDate)
    }

    private fun getPhotos(db:DayPhotoDao, start: String, end: String){
        var api = getApi()
        if (api != null) {
            Log.d(TAG, "Get photos: $start - $end")

            try {
                CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
                    val photos: List<DayPhoto> =
                        api.getAPODS(
                            start,
                            end,
                            nasaConfig.getHd(),
                            nasaConfig.getApiKey()
                        )

                    // to avoid duplicated insert
                    photos.forEach {
                        Log.d(ContentValues.TAG, it.explanation)
                        var count = db.getPhotoOfThatDay(it.date)
                        if (count == 0) db.insert(it)
                    }

                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "${e.message}")
            }
        }

    }
}