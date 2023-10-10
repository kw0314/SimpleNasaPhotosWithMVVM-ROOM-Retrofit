package com.mingolab.myapplication.repository.services

import android.content.ContentValues
import android.util.Log
import com.mingolab.myapplication.repository.localDB.DayPhoto
import com.mingolab.myapplication.repository.localDB.DayPhotoDao
import com.mingolab.myapplication.repository.remoteDB.JsonNasaApi
import com.mingolab.myapplication.repository.remoteDB.NasaConfig
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
    var API: JsonNasaApi?=null

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

    public fun getTodayPhoto(db: DayPhotoDao) {
        var api = getApi()

        var today:String = LocalDate.now(ZoneId.of(TIME_ZONE)).toString()
        nasaConfig.setEndDate(today)
        Log.d(ContentValues.TAG, today)

        if (api != null) {
            try {

                CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
                    var photo =
                        api.getAPOD(
                            nasaConfig.getEndDate(),
                            nasaConfig.getHd(),
                            nasaConfig.getApiKey()
                        )
                    Log.d(ContentValues.TAG, photo.explanation)

                    // need to check before sending a query

                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "${e.message}")
            }
        }
    }

    public fun getDayPhoto(db: DayPhotoDao, date:String){

        var api = getApi()

        if (api != null) {
            try {

                CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
                    var photo =
                        api.getAPOD(
                            date,
                            nasaConfig.getHd(),
                            nasaConfig.getApiKey()
                        )
                    Log.d(ContentValues.TAG, photo.explanation)

                    // to avoid duplicated insert
                    var count: Int = db.getPhotoOfThatDay(photo.date)
                    if (count == 0) db.insert(photo)

                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "${e.message}")
            }
        }
    }

    public fun getTodayPhotos(db:DayPhotoDao) {

        var api = getApi()


        if (api != null) {

            var today=nasaConfig.getEndDate()

            if (today!=LocalDate.now(ZoneId.of(TIME_ZONE)).toString()){
                nasaConfig.setStartDate(today)
                nasaConfig.setEndDate(LocalDate.now(ZoneId.of(TIME_ZONE)).toString())
            }
//            nasaConfig.setStartDate(LocalDate.now(ZoneId.of(TIME_ZONE)).minusDays(7).toString())

            Log.d(ContentValues.TAG, nasaConfig.getEndDate())

            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val photos: List<DayPhoto> =
                        api.getAPODS(
                            nasaConfig.getStartDate(),
                            nasaConfig.getEndDate(),
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

    public fun getLastPhotos(db:DayPhotoDao) {

        var api = getApi()


        if (api != null) {

            var lastEndDay=nasaConfig.getLastDate()
            var lastStartYYYY=lastEndDay.substring(0,4).toInt()
            var lastStartMM=lastEndDay.substring(6,8).toInt()
            var lastStartDD=lastEndDay.substring(9,11).toInt()
            var lastStartDay = LocalDate.of(lastStartYYYY,lastStartMM,lastStartDD).minusDays(7).toString()
            nasaConfig.setLastDate(lastStartDay)

            Log.d(ContentValues.TAG, "Get old photos: from $lastEndDay - ${nasaConfig.getLastDate()}")

            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val photos: List<DayPhoto> =
                        api.getAPODS(
                            nasaConfig.getLastDate(),
                            lastEndDay,
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