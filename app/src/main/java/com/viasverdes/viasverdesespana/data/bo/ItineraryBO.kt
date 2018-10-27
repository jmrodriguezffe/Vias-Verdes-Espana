package com.viasverdes.viasverdesespana.data.bo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "itinerary")
data class ItineraryBO(
      @PrimaryKey val id: Long,
      val codVV: String,
      var name: String,
      var length: Float,
      var provinces: String,
      var ca: String,
      var localization: String,
      var naturaText: String,
      var userTypes: String
) : Parcelable