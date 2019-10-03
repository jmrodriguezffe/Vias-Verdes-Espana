package com.viasverdes.viasverdesespana.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.viasverdes.viasverdesespana.BuildConfig
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.data.dao.ItineraryDAO
import com.viasverdes.viasverdesespana.work.ImportItinerariesWorker

@Database(entities = arrayOf(ItineraryBO::class), version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class VVDatabase : RoomDatabase() {

  abstract fun itineraryDAO(): ItineraryDAO

  companion object {
    private var INSTANCE: VVDatabase? = null

    fun getInstance(context: Context): VVDatabase? {
      if (INSTANCE == null) {
        synchronized(VVDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.applicationContext,
                VVDatabase::class.java, "vv_database.db")
                .fallbackToDestructiveMigration()
                .build()
          startImportWork(context)
        }
      }
      return INSTANCE
    }

    private fun startImportWork(context: Context) {
      val importItinerariesWork = OneTimeWorkRequestBuilder<ImportItinerariesWorker>().build()
      WorkManager.getInstance(context).enqueue(importItinerariesWork)
    }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}