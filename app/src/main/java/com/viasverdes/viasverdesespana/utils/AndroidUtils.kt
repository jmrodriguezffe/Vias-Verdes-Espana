package com.viasverdes.viasverdesespana.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

inline fun trueRes(f: () -> Unit): Boolean {
  f()
  return true
}

@Suppress("DEPRECATION")
inline fun fromHtmlCompact(html : String) : Spanned {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
  } else {
    Html.fromHtml(html)
  }
}