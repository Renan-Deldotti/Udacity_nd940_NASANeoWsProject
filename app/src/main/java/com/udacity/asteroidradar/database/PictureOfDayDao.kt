package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface PictureOfDayDao {
    // As there will be only one image of the day we can use always the replace strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureOfDay: PictureOfDay)

    // Get today picture
    @Query("SELECT * FROM picture_of_day_table ORDER BY rowid DESC LIMIT 1")
    fun getTodayPicture(): LiveData<PictureOfDay>

    // Delete everything from the table
    @Query("DELETE FROM picture_of_day_table")
    suspend fun deleteAllPictures()
}