package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.data.kml.AssetIconKmlLayer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlMultiGeometry
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.utils.getEnpKmlResource
import com.viasverdes.viasverdesespana.utils.getItineraryKmlResource


class MapFragment : VMFragment(), OnMapReadyCallback {
  companion object {
    const val ARG_ITINERARY = "ITINERARY"

    fun newInstance(itinerary: ItineraryBO?): MapFragment {
      val args = Bundle()
      if (itinerary != null) {
        args.putParcelable(ARG_ITINERARY, itinerary)
      }
      val fragment = MapFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private lateinit var mMap: GoogleMap


  override fun initializeView() {
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun getLayoutResource(): Int {
    return R.layout.activity_maps
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    arguments?.let {
      if (it.containsKey(ARG_ITINERARY)) {
        val kmlResource = getItineraryKmlResource(it.getParcelable(ARG_ITINERARY))
        if (kmlResource > 0) {
          val layer = KmlLayer(mMap, kmlResource, context)
          layer.addLayerToMap()
          mMap.setOnMapLoadedCallback { moveCameraToKml(layer) }
        }
        val enpKmlResource = getEnpKmlResource(it.getParcelable(ARG_ITINERARY))
        if (enpKmlResource > 0) {
          val layer = KmlLayer(mMap, enpKmlResource, context)
          layer.addLayerToMap()
        }
      } else {
//        for (kmlResource in getAllKmls()) {
//        val layer = KmlLayer(mMap, R.raw.capa_vias_verdes, context)
//        layer.addLayerToMap()
        val enp = AssetIconKmlLayer(mMap, R.raw.capa_vias_verdes, context)
        enp.addLayerToMap()
//        }
        moveCameraToMadrid()
      }
    }
  }

  private fun moveCameraToMadrid() {
    val madrid = LatLng(39.977413, -4.273707)
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(madrid, 5.5f))
  }

  private fun moveCameraToKml(kmlLayer: KmlLayer) {
    try {
      //Retrieve the first container in the KML layer
      var container = kmlLayer.containers.iterator().next()
      //Retrieve a nested container within the first container
      container = container.containers.iterator().next()
      //Retrieve the first placemark in the nested container
      val placemark = container.placemarks.iterator().next()
      //Retrieve a polygon object in a placemark
      val polygon = placemark.geometry as KmlMultiGeometry
      //Create LatLngBounds of the outer coordinates of the polygon
      val latlngList = polygon.geometryObject.first().geometryObject as ArrayList<LatLng>
      val builder = LatLngBounds.Builder()
      for (latLng in latlngList) {
        builder.include(latLng)
      }
      mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), ResourcesUtils.getDimen(R.dimen.margin__map).toInt()))
    } catch (e: Exception) {
      moveCameraToMadrid()
    }
  }

}