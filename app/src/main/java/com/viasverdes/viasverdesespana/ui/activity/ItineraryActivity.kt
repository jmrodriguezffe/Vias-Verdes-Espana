package com.viasverdes.viasverdesespana.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.underactivity.ActivityBuilder
import com.underlegendz.underactivity.UnderActivity
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.USER_TYPE__BICYCLE
import com.viasverdes.viasverdesespana.USER_TYPE__WALK
import com.viasverdes.viasverdesespana.USER_TYPE__WHEELCHAIR
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.ui.fragment.MapFragment
import com.viasverdes.viasverdesespana.utils.getImageResource
import com.viasverdes.viasverdesespana.utils.setVisible
import kotlinx.android.synthetic.main.activity_itinerary.*
import kotlinx.android.synthetic.main.toolbar.*

class ItineraryActivity : UnderActivity() {

  companion object {
    const val ARG_ITINERARY = "ITINERARY"

    fun start(activity: Activity, itinerary: ItineraryBO){
      val intent = Intent(activity, ItineraryActivity::class.java)
      intent.putExtra(ARG_ITINERARY, itinerary)
      activity.startActivity(intent)
      activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
  }


  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder
          .enableToolbar(false)
          .setContentLayout(R.layout.activity_itinerary)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val itinerary = intent.getParcelableExtra<ItineraryBO>(ARG_ITINERARY)
    val imageResource = getImageResource(itinerary)
    if(imageResource > 0){
      itinerary__image.setImageResource(imageResource)
    }else{
      itinerary__image.setVisible(false)
    }
    itinerary__title.text = ResourcesUtils.getString(R.string.itinerary__title, itinerary.name)
    itinerary__localization.text = itinerary.localization
    itinerary__provinces.text = itinerary.provinces
    itinerary__length.text = ResourcesUtils.getString(R.string.km, itinerary.length)
    itinerary__walk_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WALK))
    itinerary__bicycle_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__BICYCLE))
    itinerary__wheelchair_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WHEELCHAIR))
    itinerary__natura.text = itinerary.naturaText
    itinerary__back.setOnClickListener { onBackPressed() }
    itinerary__see_in_map.setOnClickListener { MapActivity.start(this, itinerary) }
  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
  }
}
