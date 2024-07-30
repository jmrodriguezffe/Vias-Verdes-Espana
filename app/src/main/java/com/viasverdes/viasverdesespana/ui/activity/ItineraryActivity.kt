package com.viasverdes.viasverdesespana.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.google.maps.android.data.kml.CustomKmlParser
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.utils.ScreenUtils
import com.underlegendz.underactivity.ActivityBuilder
import com.viasverdes.viasverdesespana.*
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.databinding.ActivityItineraryBinding
import com.viasverdes.viasverdesespana.ui.fragment.HowToGetDialogFragment
import com.viasverdes.viasverdesespana.utils.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import kotlin.math.min


class ItineraryActivity : TextSizeThemeActivity() {

  companion object {
    const val ARG_ITINERARY = "ITINERARY"

    fun start(
      activity: Activity,
      itinerary: ItineraryBO
    ) {
      val intent = Intent(activity, ItineraryActivity::class.java)
      intent.putExtra(ARG_ITINERARY, itinerary)
      activity.startActivity(intent)
      activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
  }

  private lateinit var binding: ActivityItineraryBinding


  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder
      .enableToolbar(false)
      .setContentLayout(R.layout.activity_itinerary)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val itinerary = intent.getParcelableExtra<ItineraryBO>(ARG_ITINERARY)
    if (itinerary == null) {
      onBackPressed()
      return
    }

    binding = ActivityItineraryBinding.bind(window.decorView.findViewById(R.id.itinerary__activity))

    registerForContextMenu(binding.optionTextSize)
    binding.optionTextSize.setOnClickListener { it.showContextMenu() }

    loadImage(
      binding.itineraryImage,
      getRemoteImageUri(itinerary),
      getRemoteImageUri(itinerary, true)
    )

    binding.itineraryTitle.text =
      ResourcesUtils.getString(R.string.itinerary__title, itinerary.name)
    binding.itineraryLocalization.text = itinerary.localization
    binding.itineraryProvinces.text = itinerary.provinces
    binding.itineraryLength.text = ResourcesUtils.getString(R.string.km, itinerary.length)
    binding.itineraryWalkUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__WALK))
    binding.itineraryBicycleUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__BICYCLE))
    binding.itineraryWheelchairUserType.setVisible(
      itinerary.userTypes.contains(
        USER_TYPE__WHEELCHAIR
      )
    )
    binding.itineraryRollerUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__ROLLER))
    binding.itineraryHorseUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__HORSE))
    binding.itineraryNaturaLabel.setVisible(!itinerary.naturaText.isNullOrEmpty())
    binding.itineraryNatura.setVisible(!itinerary.naturaText.isNullOrEmpty())
    binding.itineraryNatura.text = itinerary.naturaText
    binding.itineraryBack.setOnClickListener { onBackPressed() }
    binding.itinerarySeeInMap.setOnClickListener { MapActivity.start(this, itinerary) }
    binding.itineraryHowToGet.setOnClickListener { goTo() }
    binding.itineraryMoreInfo.setVisible(itinerary.webLink() < 500)
    binding.itineraryMoreInfo.setOnClickListener { moreInfo() }
    binding.itineraryScroll.viewTreeObserver.addOnScrollChangedListener {
      val scrollY = binding.itineraryScroll.scrollY.toFloat()
      val alpha = min(1f, scrollY / ScreenUtils.width() + 0.4f)
      binding.itineraryTitleBg.alpha = alpha
      binding.itineraryTitleShadow.alpha = alpha
    }
    val hasConnections = itinerary.connections != null
    binding.itineraryConnections.setVisible(hasConnections)
    binding.itineraryConnectionsLabel.setVisible(hasConnections)
    if (hasConnections) {
      binding.itineraryConnections.text = itinerary.connections.fromHtml()
      binding.itineraryConnections.movementMethod = LinkMovementMethod.getInstance()
    }
    if (!itinerary.accesibilityText.isNullOrEmpty()) {
      binding.itineraryAccesibilityInfo.text = itinerary.accesibilityText
    }
    if (!itinerary.unescoText.isNullOrEmpty()) {
      binding.itineraryUnesco.text = itinerary.unescoText.fromHtml()
      binding.itineraryUnesco.movementMethod = LinkMovementMethod.getInstance()
    } else {
      binding.itineraryUnesco.setVisible(false)
      binding.itineraryUnescoIcon.setVisible(false)
      binding.itineraryUnescoLabel.setVisible(false)
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

      val latLngList = getCoordinateListOnLayer(parser.containers)

      HowToGetDialogFragment.newInstance(latLngList.first(), latLngList.last())
        .show(supportFragmentManager, "how_to_get")
    }
  }

  fun moreInfo() {
    val itinerary = intent.getParcelableExtra<ItineraryBO>(ARG_ITINERARY)
    val moreInfoUri =
      Uri.parse("https://www.viasverdes.com/itinerarios/itinerario.asp?id=" + itinerary?.webLink())
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
