package com.viasverdes.viasverdesespana.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlMultiGeometry
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.databinding.FragmentMapBinding
import com.viasverdes.viasverdesespana.ui.activity.ItineraryActivity
import com.viasverdes.viasverdesespana.utils.*


class MapFragment : VMFragment(), OnMapReadyCallback, Layer.OnFeatureClickListener {

  companion object {
    const val ARG_ITINERARY = "ITINERARY"
    const val LOCATION_REQUEST_CODE = 786

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
  private lateinit var binding: FragmentMapBinding
  private var customTileProvider: TileOverlay? = null


  override fun initializeView() {
    binding = FragmentMapBinding.bind(requireView())
    val mapFragment = childFragmentManager.findFragmentById(R.id.map__view__map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    binding.mapBtnInfoClose.setOnClickListener { showInfoPanel(false) }
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__map
  }

  override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
  ) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.map_layers, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action__layer__mtn -> trueRes { loadMtnRasterLayer() }
      R.id.action__layer__roadmap -> trueRes { loadGoogleLayer(GoogleMap.MAP_TYPE_NORMAL) }
      R.id.action__layer__satellite -> trueRes { loadGoogleLayer(GoogleMap.MAP_TYPE_SATELLITE) }
      R.id.action__layer__hybrid -> trueRes { loadGoogleLayer(GoogleMap.MAP_TYPE_HYBRID) }
      R.id.action__layer__terrain -> trueRes { loadGoogleLayer(GoogleMap.MAP_TYPE_TERRAIN) }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun loadMtnRasterLayer() {
    mMap.mapType = GoogleMap.MAP_TYPE_NONE
    customTileProvider = mMap.addTileOverlay(TileOverlayOptions().zIndex(-1f).tileProvider(MtnRasterTileProvider()))
  }

  private fun loadGoogleLayer(mapType: Int) {
    customTileProvider?.remove()
    customTileProvider = null
    mMap.mapType = mapType
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    checkMyLocation()

    arguments?.let { bundle ->
      if (bundle.containsKey(ARG_ITINERARY)) {
        val itinerary = bundle.getParcelable<ItineraryBO>(ARG_ITINERARY)
        itinerary?.let { addItineraryToMap(it, centerMap = true, addEnp = true) }
      } else {
        context?.let { ctx ->
          VVDatabase.getInstance(ctx)?.itineraryDAO()?.getAllLiveData()?.observe(this,
                Observer { itinerary ->
                  if (itinerary.isNotNullOrEmpty()) {
                    itinerary?.forEach {
                      addItineraryToMap(it, centerMap = false, addEnp = false)
                    }
                    moveCameraToMadrid()
                    mMap.setOnInfoWindowClickListener {
                      activity?.let { it1 -> ItineraryActivity.start(it1, it.tag as ItineraryBO) }
                    }
                  }
                })
        }
      }
    }
  }

  private fun checkMyLocation() {
    context?.let {
      val permission = PermissionChecker.checkSelfPermission(it,
            Manifest.permission.ACCESS_FINE_LOCATION)
      if (permission == PermissionChecker.PERMISSION_GRANTED) {
        mMap.isMyLocationEnabled = true
      } else if (activity != null && requireActivity().isFinishing.not()) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
      }
    }
  }

  override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
  ) {
    if (requestCode == LOCATION_REQUEST_CODE) {
      if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        checkMyLocation()
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
  }

  private fun addItineraryToMap(
        itinerary: ItineraryBO,
        centerMap: Boolean,
        addEnp: Boolean
  ) {
    val kmlResource = getItineraryKmlResource(itinerary)
    if (kmlResource > 0) {
      val layer = KmlLayer(mMap, kmlResource, context)
      layer.addLayerToMap()
      if (centerMap) {
        mMap.setOnMapLoadedCallback { moveCameraToKml(layer) }
      } else {
        insertMarkerOnFirstCoordinate(itinerary, layer)
      }
    }
    if (addEnp) {
      val enpKmlResource = getEnpKmlResource(itinerary)
      if (enpKmlResource > 0) {
        val layer = KmlLayer(mMap, enpKmlResource, context)
        layer.setOnFeatureClickListener(this)
        layer.addLayerToMap()
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
      val latLng = getFirstCoordinateOnLayer(kmlLayer.containers)
      mMap.addMarker(MarkerOptions()
            .position(latLng)
            .title(itinerary.name)
            .snippet(getString(R.string.map__marker_more_info))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker__itinerary))
      )?.tag = itinerary
    } catch (e: Exception) {
      // Nothing to do
    }
  }

  private fun showInfoPanel(show: Boolean) {
    binding.mapContainerInfo.setVisible(show)
  }

  override fun onFeatureClick(feature: Feature?) {
    showInfoPanel(true)
    feature?.getProperty("description")?.let {
      binding.mapWebInfoDescription.loadDataWithBaseURL(null, it, "text/html", "utf-8", null)
    }
  }

}