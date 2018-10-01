package com.viasverdes.viasverdesespana.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.viasverdes.viasverdesespana.data.dbo.ProvinceDBO
import com.viasverdes.viasverdesespana.data.dbo.UserTypeDBO
import com.viasverdes.viasverdesespana.data.dbo.ViaVerdeDBO

@Dao
interface ProvinceDAO {
    @Query("SELECT * from province")
    fun getAll(): List<ProvinceDBO>

    @Query("SELECT * from province where id IN (:ids)")
    fun getById(ids : LongArray): List<ProvinceDBO>

    @Insert(onConflict = REPLACE)
    fun insert(viaVerde: ProvinceDBO)

    @Query("DELETE from province")
    fun deleteAll()
}