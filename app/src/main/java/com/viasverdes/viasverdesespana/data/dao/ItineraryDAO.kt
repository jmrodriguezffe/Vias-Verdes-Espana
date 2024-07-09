package com.viasverdes.viasverdesespana.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO

@Dao
interface ItineraryDAO {
  @Query("SELECT * from itinerary")
  fun getAll(): List<ItineraryBO>

  @Query("SELECT * from itinerary ORDER BY provinces ASC")
  fun getAllLiveData(): LiveData<List<ItineraryBO>>

  @Query("SELECT * from itinerary where id=:id")
  fun getById(id: Long): LiveData<ItineraryBO>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(itinerary: ItineraryBO)

  @Query("DELETE from itinerary")
  fun deleteAll()
}