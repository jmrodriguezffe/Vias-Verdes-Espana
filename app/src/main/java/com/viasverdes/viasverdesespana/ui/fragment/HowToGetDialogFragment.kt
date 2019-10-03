package com.viasverdes.viasverdesespana.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng
import com.viasverdes.viasverdesespana.R

class HowToGetDialogFragment : DialogFragment() {

  companion object {
    private const val KEY_START_COORDINATES = "COORDINATES"
    private const val KEY_END_COORDINATES = "COORDINATES"
    private const val KEY_START_POINT = "START_POINT"
    private const val KEY_TRANSPORT_MODE = "TRANSPORT_MODE"

    private const val TRANSPORT_WALK = "w"
    private const val TRANSPORT_BIKE = "b"
    private const val TRANSPORT_CAR = "d"

    fun newInstance(startCoordinates: LatLng,
                    endCoordinates: LatLng
    ): HowToGetDialogFragment {
      val args = Bundle()
      args.putParcelable(KEY_START_COORDINATES, startCoordinates)
      args.putParcelable(KEY_END_COORDINATES, endCoordinates)
      val fragment = HowToGetDialogFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private var startCoordinates: LatLng? = null
  private var endCoordinates: LatLng? = null
  private var startPoint = true
  private var transportMode = TRANSPORT_CAR
  private lateinit var startView: View
  private lateinit var endView: View
  private lateinit var carView: View
  private lateinit var bikeView: View
  private lateinit var walkView: View
  private lateinit var goView: View

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(activity!!)
    val view = activity?.layoutInflater?.inflate(R.layout.dialog__how_to_get, null)
    builder.setView(view)

    if (savedInstanceState != null) {
      startPoint = savedInstanceState.getBoolean(KEY_START_POINT, true)
      transportMode = savedInstanceState.getString(KEY_TRANSPORT_MODE, TRANSPORT_CAR)
      startCoordinates = savedInstanceState.getParcelable(KEY_START_COORDINATES)
      endCoordinates = savedInstanceState.getParcelable(KEY_END_COORDINATES)
    } else if (arguments != null) {
      startCoordinates = arguments!!.getParcelable(KEY_START_COORDINATES)
      endCoordinates = arguments!!.getParcelable(KEY_END_COORDINATES)
    }

    initializeView(view!!)
    initializeActions()
    setRoutePoint(startPoint)
    setTransportMode(TRANSPORT_CAR)

    return builder.create()
  }

  private fun initializeView(view: View) {
    startView = view.findViewById(R.id.how_to_get__route_point__start)
    endView = view.findViewById(R.id.how_to_get__route_point__end)
    carView = view.findViewById(R.id.how_to_get__transport_mode__car)
    bikeView = view.findViewById(R.id.how_to_get__transport_mode__bike)
    walkView = view.findViewById(R.id.how_to_get__transport_mode__walk)
    goView = view.findViewById(R.id.how_to_get__btn__go)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelable(KEY_START_COORDINATES, startCoordinates)
    outState.putParcelable(KEY_END_COORDINATES, endCoordinates)
    outState.putBoolean(KEY_START_POINT, startPoint)
    outState.putString(KEY_TRANSPORT_MODE, transportMode)
  }

  private fun initializeActions() {
    startView.setOnClickListener {
      setRoutePoint(true)
    }
    endView.setOnClickListener {
      setRoutePoint(false)
    }

    carView.setOnClickListener {
      setTransportMode(TRANSPORT_CAR)
    }

    walkView.setOnClickListener {
      setTransportMode(TRANSPORT_WALK)
    }

    bikeView.setOnClickListener {
      setTransportMode(TRANSPORT_BIKE)
    }

    goView.setOnClickListener {
      val coordinates = if (startPoint) startCoordinates else endCoordinates
      val gmmIntentUri = Uri.parse("google.navigation:q=" + coordinates?.latitude + "," + coordinates?.longitude + "&mode=" + transportMode)
      val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
      mapIntent.setPackage("com.google.android.apps.maps")
      startActivity(mapIntent)
    }
  }

  private fun setRoutePoint(startPoint: Boolean) {
    this.startPoint = startPoint
    endView.isSelected = !startPoint
    startView.isSelected = startPoint
  }

  private fun setTransportMode(transportMode: String) {
    this.transportMode = transportMode
    carView.isSelected = TRANSPORT_CAR == transportMode
    walkView.isSelected = TRANSPORT_WALK == transportMode
    bikeView.isSelected = TRANSPORT_BIKE == transportMode
  }
}
