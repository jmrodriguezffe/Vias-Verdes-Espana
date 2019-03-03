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

fun getRemoteImageUri(itinerary: ItineraryBO): Uri {
  val application = CoreApplication.get()
  val url = application.getResources()
        .getString(R.string.remote_image_url, itinerary.codVV.toLowerCase())
  return Uri.parse(url)
}

fun getRemoteImageThumbUri(itinerary: ItineraryBO): Uri {
  val application = CoreApplication.get()
  val url = application.getResources()
        .getString(R.string.remote_image_thumbnail_url, itinerary.codVV.toLowerCase())
  return Uri.parse(url)
}

fun getAltimetricResource(itinerary: ItineraryBO): Int {
  val application = CoreApplication.get()
  return application.getResources().getIdentifier(
        "altimetric__" + itinerary.codVV.toLowerCase(), "drawable", application.getPackageName())
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

fun getProvinceFromCA(ca: Int): Int {
  return when (ca) {
    1 -> R.array.provinces_andalucia
    2 -> R.array.provinces_aragon
    3 -> R.array.provinces_asturias
    4 -> R.array.provinces_cantabria
    5 -> R.array.provinces_castillalamancha
    6 -> R.array.provinces_castillaleon
    7 -> R.array.provinces_catalunya
    8 -> R.array.provinces_extremadura
    9 -> R.array.provinces_galicia
    10 -> R.array.provinces_baleares
    11 -> R.array.provinces_larioja
    12 -> R.array.provinces_madrid
    13 -> R.array.provinces_murcia
    14 -> R.array.provinces_navarra
    15 -> R.array.provinces_paisvasco
    16 -> R.array.provinces_valencia
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