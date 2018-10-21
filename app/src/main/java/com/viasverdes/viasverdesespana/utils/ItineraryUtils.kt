package com.viasverdes.viasverdesespana.utils

import com.underlegendz.corelegendz.CoreApplication
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO


fun getImageResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "image__" + itinerary.codVV.toLowerCase(), "drawable", application.getPackageName())
}

fun getItineraryKmlResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "itinerary__" + itinerary.codVV.toLowerCase(), "raw", application.getPackageName())
}

fun getEnpKmlResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "enp__" + itinerary.codVV.toLowerCase(), "raw", application.getPackageName())
}

fun getAllKmls(): IntArray {
  return intArrayOf(R.raw.itinerary__acei, R.raw.itinerary__sier)
}