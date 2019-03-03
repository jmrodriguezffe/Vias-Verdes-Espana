package com.viasverdes.viasverdesespana.utils

import android.net.Uri
import android.os.Build
import android.support.v4.widget.CircularProgressDrawable
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.viasverdes.viasverdesespana.R
import kotlinx.android.synthetic.main.activity_itinerary.*
import java.text.Normalizer

inline fun trueRes(f: () -> Unit): Boolean {
  f()
  return true
}

@Suppress("DEPRECATION")
fun fromHtmlCompact(html : String) : Spanned {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
  } else {
    Html.fromHtml(html)
  }
}

fun String.search(other: CharSequence, ignoreCase: Boolean = true, ignoreAccent : Boolean = true): Boolean {
  var thisValue = this
  var otherValue = other as String
  if(ignoreAccent){
    thisValue = thisValue.stripAccents()
    otherValue = otherValue.stripAccents()
  }
  return thisValue.contains(otherValue, ignoreCase)
}

fun String.stripAccents() : String {
  var string = Normalizer.normalize(this, Normalizer.Form.NFD)
  string = Regex("\\p{InCombiningDiacriticalMarks}+").replace(string, "")
  return  string
}

fun loadImage(uri: Uri, imageView: ImageView) {
  val circularProgressDrawable = CircularProgressDrawable(imageView.context)
  circularProgressDrawable.strokeWidth = 5f
  circularProgressDrawable.centerRadius = 30f
  circularProgressDrawable.setColorSchemeColors(0xff789E0C.toInt())
  circularProgressDrawable.start()

  Glide.with(imageView)
        .load(uri)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic__itinerary__no_image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}