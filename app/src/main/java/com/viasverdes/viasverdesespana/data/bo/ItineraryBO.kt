package com.viasverdes.viasverdesespana.data.bo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
      var userTypes: String,
      var connections: String?,
      var accesibilityText: String?
) : Parcelable