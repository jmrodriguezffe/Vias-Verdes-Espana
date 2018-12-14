package com.viasverdes.viasverdesespana.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.maps.android.data.kml.CustomKmlParser
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.utils.ScreenUtils
import com.underlegendz.underactivity.ActivityBuilder
import com.underlegendz.underactivity.UnderActivity
import com.viasverdes.viasverdesespana.*
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.utils.*
import kotlinx.android.synthetic.main.activity_itinerary.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream


class ItineraryActivity : UnderActivity() {

  companion object {
    const val ARG_ITINERARY = "ITINERARY"

    fun start(activity: Activity,
              itinerary: ItineraryBO
    ) {
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
    if (imageResource > 0) {
      itinerary__image.setImageResource(imageResource)
    } else {
      itinerary__image.setImageResource(R.drawable.ic__itinerary__no_image)
//      itinerary__image.setVisible(false)
    }

    val altimetricResource = getAltimetricResource(itinerary)
    if (altimetricResource > 0) {
      itinerary__altimetric__img.setImageResource(altimetricResource)
      itinerary__altimetric.setVisible(true)
    } else {
      itinerary__altimetric.setVisible(false)
    }

    itinerary__title.text = ResourcesUtils.getString(R.string.itinerary__title, itinerary.name)
    itinerary__localization.text = itinerary.localization
    itinerary__provinces.text = itinerary.provinces
    itinerary__length.text = ResourcesUtils.getString(R.string.km, itinerary.length)
    itinerary__walk_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WALK))
    itinerary__bicycle_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__BICYCLE))
    itinerary__wheelchair_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WHEELCHAIR))
    itinerary__roller_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__ROLLER))
    itinerary__horse_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__HORSE))
    itinerary__natura.text = itinerary.naturaText
    itinerary__back.setOnClickListener { onBackPressed() }
    itinerary__see_in_map.setOnClickListener { MapActivity.start(this, itinerary) }
    itinerary__how_to_get.setOnClickListener { goTo() }
    itinerary__more_info.setOnClickListener { moreInfo() }
    itinerary__scroll.viewTreeObserver.addOnScrollChangedListener {
      val scrollY = itinerary__scroll.scrollY.toFloat()
      val alpha = Math.min(1f, scrollY / ScreenUtils.width() + 0.4f)
      itinerary__title_bg.alpha = alpha
      itinerary__title_shadow.alpha = alpha
    }

  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
  }

  fun goTo() {
    val itinerary = intent.getParcelableExtra<ItineraryBO>(ARG_ITINERARY)
    val kmlResource = getItineraryKmlResource(itinerary)
    if (kmlResource > 0) {
      val stream = getResources().openRawResource(kmlResource)
      val xmlPullParser = createXmlParser(stream)
      val parser = CustomKmlParser(xmlPullParser)
      parser.parseKml()
      stream.close()

      val latLng = getFirstCoordinateOnLayer(parser.containers)
      val gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude)
      val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
      mapIntent.setPackage("com.google.android.apps.maps")
      startActivity(mapIntent)
    }
  }

  fun moreInfo() {
    val itinerary = intent.getParcelableExtra<ItineraryBO>(ARG_ITINERARY)
    val moreInfoUri = Uri.parse("http://www.viasverdes.com/rednatura2000/itinerarios/itinerario.asp?id=" + itinerary.id)
    val mapIntent = Intent(Intent.ACTION_VIEW, moreInfoUri)
    startActivity(mapIntent)
  }

  @Throws(XmlPullParserException::class)
  private fun createXmlParser(stream: InputStream): XmlPullParser {
    val factory = XmlPullParserFactory.newInstance()
    factory.isNamespaceAware = true
    val parser = factory.newPullParser()
    parser.setInput(stream, null as String?)
    return parser
  }
}
