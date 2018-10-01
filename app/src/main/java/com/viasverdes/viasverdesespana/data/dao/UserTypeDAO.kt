package com.viasverdes.viasverdesespana.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.viasverdes.viasverdesespana.data.dbo.UserTypeDBO
import com.viasverdes.viasverdesespana.data.dbo.ViaVerdeDBO

@Dao
interface UserTypeDAO {
    @Query("SELECT * from user_type")
    fun getAll(): List<UserTypeDBO>

    @Query("SELECT * from user_type where id IN (:ids)")
    fun getById(ids : LongArray): List<UserTypeDBO>

    @Insert(onConflict = REPLACE)
    fun insert(viaVerde: UserTypeDBO)

    @Query("DELETE from user_type")
    fun deleteAll()
}