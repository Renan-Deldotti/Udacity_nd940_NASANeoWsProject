package com.udacity.asteroidradar.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {

    // Get today Asteroids (to menu option)
    private fun today(): String {
        val today = LocalDate.now()
        return today.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

    // Limit the search to this week Asteroids (to menu option)
    private fun todayPlusSevenDays() : String {
        val today = LocalDate.now()
        val inSevenDays = today.plusDays(7)
        return inSevenDays.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

    /* ----- Get data from the API and save in DB ----- */

    // As the most far date we want is "Week" we can pass 7 days as start date
    suspend fun fetchAsteroidFromAPI() {
        withContext(Dispatchers.IO) {
            val lastSevenDaysAsteroids = NasaAPI().getAPI().getAsteroidsWithinDate(today(), todayPlusSevenDays())
            val jsonResult = parseAsteroidsJsonResult(JSONObject(lastSevenDaysAsteroids))
            database.asteroidDao.insertAll(*jsonResult.toTypedArray())
        }
    }

    suspend fun fetchTodayPictureFromAPI() {
        withContext(Dispatchers.IO) {
            val todayPicture = NasaAPI().getAPI().getTodayImage()
            // Log.d("AsteroidRepository", "fetchTodayPictureFromAPI: is today picture a image: ${Constants.IMAGE_MEDIA_TYPE == todayPicture.mediaType}")
            database.pictureOfDayDao.insert(todayPicture)
        }
    }



    // Get data from DB to display
    val allAsteroidsSorted: LiveData<List<Asteroid>> = database.asteroidDao.getAllAsteroidsSortedByApproachDateDesc()

    // Get only today Asteroids from DB
    val allAsteroidsFromToday: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroidsFromPeriod(today(), today())

    // Get only week Asteroids from DB
    val allAsteroidsFromWeek: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroidsFromPeriod(today(), todayPlusSevenDays())

    // Get the today's image - later
    val todayImage: LiveData<PictureOfDay> = database.pictureOfDayDao.getTodayPicture()
}