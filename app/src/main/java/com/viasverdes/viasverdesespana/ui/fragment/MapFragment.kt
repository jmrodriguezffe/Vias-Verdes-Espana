package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R

class MapFragment : VMFragment(), OnMapReadyCallback {

    companion object {

        fun newInstance(): MapFragment {
            val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
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

        val layer = KmlLayer(mMap, R.raw.capa_vias_verdes, context)
        layer.addLayerToMap()

        val madrid = LatLng(39.977413, -4.273707)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(madrid, 5.5f))
    }

}