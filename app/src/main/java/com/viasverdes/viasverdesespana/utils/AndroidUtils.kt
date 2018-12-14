package com.viasverdes.viasverdesespana.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
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
  val out = Normalizer.normalize(this, Normalizer.Form.NFD)
  return out.replace("[^\\p{ASCII}]", "")
}