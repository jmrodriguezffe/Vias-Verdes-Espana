package com.viasverdes.viasverdesespana.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.underlegendz.corelegendz.utils.ListUtils.isEmpty
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.data.dao.ItineraryDAO
import com.viasverdes.viasverdesespana.work.ImportItinerariesWorker

@Database(entities = arrayOf(ItineraryBO::class), version = 1, exportSchema = false)
abstract class VVDatabase : RoomDatabase() {

  abstract fun itineraryDAO(): ItineraryDAO

  companion object {
    private var INSTANCE: VVDatabase? = null

    fun getInstance(context: Context): VVDatabase? {
      if (INSTANCE == null) {
        synchronized(VVDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                VVDatabase::class.java, "vv_database.db")
                .build()
          startImportWork()
        }
      }
      return INSTANCE
    }

    private fun startImportWork() {
      val importItinerariesWork = OneTimeWorkRequestBuilder<ImportItinerariesWorker>().build()
      WorkManager.getInstance().enqueue(importItinerariesWork)
    }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}