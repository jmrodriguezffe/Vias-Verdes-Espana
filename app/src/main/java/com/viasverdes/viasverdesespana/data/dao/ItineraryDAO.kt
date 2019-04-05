package com.viasverdes.viasverdesespana.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO

@Dao
interface ItineraryDAO {
  @Query("SELECT * from itinerary")
  fun getAll(): List<ItineraryBO>

  @Query("SELECT * from itinerary ORDER BY provinces ASC")
  fun getAllLiveData(): LiveData<List<ItineraryBO>>

  @Query("SELECT * from itinerary where id=:id")
  fun getById(id: Long): LiveData<ItineraryBO>

  @Insert(onConflict = REPLACE)
  fun insert(itinerary: ItineraryBO)

  @Query("DELETE from itinerary")
  fun deleteAll()
}