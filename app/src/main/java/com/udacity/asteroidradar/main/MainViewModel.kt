package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
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
                Log.e("MainViewModel", "Exception: ${e.message}")
                Toast.makeText(application.applicationContext, "NW ISSUE", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val asteroids: LiveData<List<Asteroid>> = repository.allAsteroidsFromWeek

    val todayImage: LiveData<PictureOfDay> = repository.todayImage
}