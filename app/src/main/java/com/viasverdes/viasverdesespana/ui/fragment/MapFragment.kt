package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.kml.AssetIconKmlLayer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlMultiGeometry
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.utils.getEnpKmlResource
import com.viasverdes.viasverdesespana.utils.getItineraryKmlResource
import com.viasverdes.viasverdesespana.utils.setVisible
import kotlinx.android.synthetic.main.fragment__map.*


class MapFragment : VMFragment(), OnMapReadyCallback, Layer.OnFeatureClickListener {

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
    val mapFragment = childFragmentManager.findFragmentById(R.id.map__view__map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    map__btn__info_close.setOnClickListener { showInfoPanel(false) }
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__map
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    arguments?.let {
      if (it.containsKey(ARG_ITINERARY)) {
        val itinerary = it.getParcelable<ItineraryBO>(ARG_ITINERARY)
        val kmlResource = getItineraryKmlResource(itinerary)
        if (kmlResource > 0) {
          val layer = KmlLayer(mMap, kmlResource, context)
          layer.addLayerToMap()
          mMap.setOnMapLoadedCallback { moveCameraToKml(layer) }
        }
        val enpKmlResource = getEnpKmlResource(itinerary)
        if (enpKmlResource > 0) {
          val layer = AssetIconKmlLayer(mMap, enpKmlResource, context)
          layer.setOnFeatureClickListener(this)
          layer.addLayerToMap()
        }
      } else {
//        for (kmlResource in getAllKmls()) {
//        val layer = KmlLayer(mMap, R.raw.capa_vias_verdes, context)
//        layer.addLayerToMap()
//        val enp = KmlLayer(mMap, R.raw.capa_vias_verdes, context)
//        enp.addLayerToMap()
////        }
//        moveCameraToMadrid()
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

  private fun insertMarkerOnFirstCoordinate(
        itinerary: ItineraryBO,
        kmlLayer: KmlLayer
  ) {
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

      mMap.addMarker(
            MarkerOptions().position(latlngList.first())
                  .title(itinerary.name)
      ).tag = itinerary.id
    } catch (e: Exception) {
      // nothing to do
    }
  }

  private fun showInfoPanel(show: Boolean) {
    map__container__info.setVisible(show)
  }

  override fun onFeatureClick(feature: Feature?) {
    showInfoPanel(true)
    feature?.getProperty("description")?.let {
      map__web__info_description.loadDataWithBaseURL(null, it, "text/html", "utf-8", null)
    }
  }

}