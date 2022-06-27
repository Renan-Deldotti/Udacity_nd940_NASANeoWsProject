package com.udacity.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : ViewModel() {

    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            try {
                repository.fetchAsteroidFromAPI()
                repository.fetchTodayPictureFromAPI()
            } catch (e: Exception) {
                Toast.makeText(application.applicationContext, R.string.no_internet_connection_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val todayImage: LiveData<PictureOfDay> = repository.todayImage

    enum class AsteroidFilters {TODAY_ASTEROIDS, WEEK_ASTEROIDS, SAVED_ASTEROIDS}
    private val currentFilter = MutableLiveData(AsteroidFilters.SAVED_ASTEROIDS)
    val asteroids = Transformations.switchMap(currentFilter) { filter ->
        when(filter) {
            AsteroidFilters.TODAY_ASTEROIDS -> repository.allAsteroidsFromToday
            AsteroidFilters.WEEK_ASTEROIDS -> repository.allAsteroidsFromWeek
            else -> repository.allAsteroidsSorted
        }
    }

    fun filterAsteroids(asteroidFilters: AsteroidFilters) {
        currentFilter.value = asteroidFilters
    }
}
