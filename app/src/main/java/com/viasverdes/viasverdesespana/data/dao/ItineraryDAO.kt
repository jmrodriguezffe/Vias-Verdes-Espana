package com.viasverdes.viasverdesespana.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.viasverdes.viasverdesespana.data.dbo.ViaVerdeDBO

@Dao
interface ItineraryDAO {
    @Query("SELECT * from itinerary")
    fun getAll(): List<ViaVerdeDBO>

    @Query("SELECT * from itinerary where id in (:ids)")
    fun getById(ids : LongArray): List<ViaVerdeDBO>

    @Insert(onConflict = REPLACE)
    fun insert(viaVerde: ViaVerdeDBO)

    @Query("DELETE from itinerary")
    fun deleteAll()
}