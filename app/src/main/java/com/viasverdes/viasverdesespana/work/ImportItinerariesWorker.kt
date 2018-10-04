package com.viasverdes.viasverdesespana.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.underlegendz.corelegendz.utils.ListUtils
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.dto.DatabaseImportDTO
import java.io.ByteArrayOutputStream
import java.io.IOException

class ImportItinerariesWorker(
      context: Context,
      params: WorkerParameters
) : Worker(context, params) {

  override fun doWork(): Result {
    val vvDatabase = VVDatabase.getInstance(applicationContext)
    if (ListUtils.isEmpty(vvDatabase?.itineraryDAO()?.getAll())) {
      val json = getRawResource(applicationContext, R.raw.itineraries)
      if (json != null) {
        val gson = Gson()
        val database = gson.fromJson(json, DatabaseImportDTO::class.java)
        for (itinerary in database.itineraries) {
          vvDatabase?.itineraryDAO()?.insert(itinerary)
        }
      }
    }
    return Result.SUCCESS
  }

  private fun getRawResource(
        ctx: Context,
        resource: Int
  ): String? {
    var res: String? = null
    val inputStream = ctx.resources.openRawResource(resource)
    val baos = ByteArrayOutputStream()
    val b = ByteArray(1)
    try {
      while (inputStream.read(b) != -1) {
        baos.write(b)
      }
      res = baos.toString()
      inputStream.close()
      baos.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }

    return res
  }
}