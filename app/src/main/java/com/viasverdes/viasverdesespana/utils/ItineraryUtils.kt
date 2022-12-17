package com.viasverdes.viasverdesespana.utils

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlContainer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlMultiGeometry
import com.underlegendz.corelegendz.CoreApplication
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO

fun getImageResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "image__" + itinerary.codVV.toLowerCase(), "drawable", application.getPackageName())
}

fun getRemoteImageUri(itinerary: ItineraryBO, alternative: Boolean = false): Uri {
  val application = CoreApplication.get()
  val url = application.getResources().getString(
      if(alternative) { R.string.remote_image_url_png } else { R.string.remote_image_url },
      itinerary.codVV.toLowerCase())
  return Uri.parse(url)
}

fun getRemoteImageThumbUri(itinerary: ItineraryBO, alternative: Boolean = false): Uri {
  val application = CoreApplication.get()
  val url = application.getResources().getString(
      if(alternative) { R.string.remote_image_thumbnail_url_png } else { R.string.remote_image_thumbnail_url },
      itinerary.codVV.toLowerCase())
  return Uri.parse(url)
}

fun getAltimetricResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "altimetric__" + itinerary.codVV.toLowerCase(), "drawable", application.getPackageName())
}

fun getItineraryKmlResource(itinerary: ItineraryBO?): Int {
  if (itinerary == null) {
    return -1
  }
  val application = CoreApplication.get()
  return application.resources.getIdentifier(
        "itinerary__" + itinerary.codVV.toLowerCase(), "raw", application.packageName)
}

fun getEnpKmlResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "enp__" + itinerary.codVV.toLowerCase(), "raw", application.getPackageName())
}

fun getAllKmls(): IntArray {
  return intArrayOf(R.raw.itinerary__acei, R.raw.itinerary__sier)
}

fun getProvinceFromCA(ca: Int): Int {
  return when (ca) {
    1 -> R.array.provinces_andalucia
    2 -> R.array.provinces_aragon
    3 -> R.array.provinces_cantabria
    4 -> R.array.provinces_castillalamancha
    5 -> R.array.provinces_castillaleon
    6 -> R.array.provinces_catalunya
    7 -> R.array.provinces_madrid
    8 -> R.array.provinces_navarra
    9 -> R.array.provinces_valencia
    10 -> R.array.provinces_extremadura
    11 -> R.array.provinces_galicia
    12 -> R.array.provinces_baleares
    13 -> R.array.provinces_canarias
    14 -> R.array.provinces_larioja
    15 -> R.array.provinces_paisvasco
    16 -> R.array.provinces_asturias
    17 -> R.array.provinces_murcia
    else -> R.array.provinces
  }
}

fun getFirstCoordinateOnLayer(kmlContainers: Iterable<KmlContainer>): LatLng {
  //Retrieve the first container in the KML layer
  var container = kmlContainers.iterator().next()
  //Retrieve a nested container within the first container
  container = container.containers.iterator().next()
  //Retrieve the first placemark in the nested container
  val placemark = container.placemarks.iterator().next()
  //Retrieve a polygon object in a placemark
  val polygon = placemark.geometry as KmlMultiGeometry
  //Create LatLngBounds of the outer coordinates of the polygon
  val latlngList = polygon.geometryObject.first().geometryObject as ArrayList<LatLng>
  val latLng = latlngList.first()
  return latLng
}

fun getCoordinateListOnLayer(kmlContainers: Iterable<KmlContainer>): ArrayList<LatLng> {
  //Retrieve the first container in the KML layer
  var container = kmlContainers.iterator().next()
  //Retrieve a nested container within the first container
  container = container.containers.iterator().next()
  //Retrieve the first placemark in the nested container
  val placemark = container.placemarks.iterator().next()
  //Retrieve a polygon object in a placemark
  val polygon = placemark.geometry as KmlMultiGeometry
  //Create LatLngBounds of the outer coordinates of the polygon
  val latlngList = polygon.geometryObject.first().geometryObject as ArrayList<LatLng>
  return latlngList
}