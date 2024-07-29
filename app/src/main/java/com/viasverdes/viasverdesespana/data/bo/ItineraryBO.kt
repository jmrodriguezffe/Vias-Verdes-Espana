package com.viasverdes.viasverdesespana.data.bo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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
      var naturaText: String?,
      var userTypes: String,
      var connections: String?,
      var accesibilityText: String?,
      var unescoText: String?,
      var order: String?,
) : Parcelable {

      fun webLink() = id % 1000 // workaround to have 2 or more itineraries with the same web id
}