package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    // Insert only one Asteroid
    @Insert
    suspend fun insert(asteroid: Asteroid)

    // Insert all and replace with conflicts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: Asteroid)

    // Update an Asteroid
    @Update
    suspend fun update(asteroid: Asteroid)

    // Get an Asteroid by a given id
    @Query("SELECT * from asteroid_table WHERE id = :id")
    fun getAsteroidById(id: Long) : LiveData<Asteroid>

    // Get all Asteroid sorted by closeApproachDate
    @Query("SELECT * from asteroid_table ORDER BY closeApproachDate DESC")
    fun getAllAsteroidsSortedByApproachDateDesc() : LiveData<List<Asteroid>>

    // Get all Asteroids within a determinate period of time (i.e. week, month, year, decade)
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate BETWEEN :initialDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsFromPeriod(initialDate: String, endDate: String) : LiveData<List<Asteroid>>

    // Delete everything from the table
    @Query("DELETE FROM asteroid_table")
    suspend fun deleteAllAsteroid()
}