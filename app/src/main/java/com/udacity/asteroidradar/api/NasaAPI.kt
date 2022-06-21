package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NasaAPI {

    // Create Moshi to be used with Retrofit
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Create a Retrofit instance
    private val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create()) // Scalars converter for Asteroid
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi for "Today image"
        .build()

    // Return the Retrofit with access to query the NasaAPI
    fun getAPI(): QueryNasaAPI = retrofitInstance.create(QueryNasaAPI::class.java)
}

interface QueryNasaAPI {

    // Calls to the API to get the Asteroids should follow this scheme:
    // https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=YOUR_API_KEY
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsWithinDate(
        @Query("start_date") startDate : String,
        @Query("end_date") endDate : String,
        @Query("api_key") apiKey : String = Constants.API_KEY
    ) : String

    // Also, we need a call to NasaAPI to get the "image of the day"
    // https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY
    @GET("planetary/apod")
    suspend fun getTodayImage (
        @Query("api_key") apiKey : String = Constants.API_KEY
    ) : PictureOfDay
}