package com.viasverdes.viasverdesespana.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.viasverdes.viasverdesespana.data.dao.ItineraryDAO
import com.viasverdes.viasverdesespana.data.dao.ProvinceDAO
import com.viasverdes.viasverdesespana.data.dao.UserTypeDAO

@Database(entities = arrayOf(ProvinceDAO::class, UserTypeDAO::class, ItineraryDAO::class), version = 1)
abstract class VVDatabase : RoomDatabase() {

    abstract fun provinceDAO(): ProvinceDAO
    abstract fun itineraryDAO(): ItineraryDAO
    abstract fun userTypeDAO(): UserTypeDAO

    companion object {
        private var INSTANCE: VVDatabase? = null

        fun getInstance(context: Context): VVDatabase? {
            if (INSTANCE == null) {
                synchronized(VVDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VVDatabase::class.java, "vv_database.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}