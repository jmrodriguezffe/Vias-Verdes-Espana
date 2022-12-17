package com.viasverdes.viasverdesespana.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.underlegendz.underactivity.ActivityBuilder
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.ui.fragment.MapFragment

class MapActivity : TextSizeThemeActivity() {

  companion object {
    const val ARG_ITINERARY = "ITINERARY"
    const val TAG_MAP = "TAG_MAP"

    fun start(activity: Activity, itinerary: ItineraryBO){
      val intent = Intent(activity, MapActivity::class.java)
      intent.putExtra(ARG_ITINERARY, itinerary)
      activity.startActivity(intent)
      activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
  }


  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder
          .enableToolbar(true)
          .setToolbar(R.layout.toolbar)
          .setToolbarBack(true)
          .setToolbarBackIcon(R.drawable.ic__back)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragment(getOrCreateMap(), TAG_MAP, false)

    toolbar.title = ""
    findViewById<TextView>(R.id.toolbar__title).setText(R.string.app_name)
  }

  private fun getOrCreateMap(): Fragment {
    return getFragment(TAG_MAP) ?: MapFragment.newInstance(intent.getParcelableExtra(ARG_ITINERARY))
  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
  }
}
