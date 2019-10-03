package com.viasverdes.viasverdesespana.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.underlegendz.corelegendz.CoreApplication
import com.viasverdes.viasverdesespana.R
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

fun loadImage(imageView: ImageView, uri: Uri, alternativeUri: Uri? = null) {
  val circularProgressDrawable = CircularProgressDrawable(imageView.context)
  circularProgressDrawable.strokeWidth = 5f
  circularProgressDrawable.centerRadius = 30f
  circularProgressDrawable.setColorSchemeColors(0xff789E0C.toInt())
  circularProgressDrawable.start()

  Glide.with(imageView)
        .load(uri)
        .placeholder(circularProgressDrawable)
        .error(
            if(alternativeUri == null){
              Glide.with(imageView).load(R.drawable.ic__itinerary__no_image)
            }else{
              Glide.with(imageView).load(alternativeUri)
            }
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

val KEY_TEXT_SIZE = "TEXT_SIZE"

@SuppressLint("ApplySharedPref")
fun saveTextSize(size : Int) {
  val preferences = PreferenceManager.getDefaultSharedPreferences(CoreApplication.get())
  preferences.edit().putInt(KEY_TEXT_SIZE, size).commit()
}

fun getTextSizeTheme(context: Context) : Int {
  val preferences = PreferenceManager.getDefaultSharedPreferences(context)
  return when(preferences.getInt(KEY_TEXT_SIZE, 0)){
    -1 -> R.style.AppTheme_TextSmall
    1 -> R.style.AppTheme_TextBig
    else -> R.style.AppTheme_TextNormal
  }
}