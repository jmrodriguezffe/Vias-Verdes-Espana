package com.viasverdes.viasverdesespana.ui.activity

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.underlegendz.underactivity.UnderActivity
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.utils.getTextSizeTheme
import com.viasverdes.viasverdesespana.utils.saveTextSize
import com.viasverdes.viasverdesespana.utils.trueRes


abstract class TextSizeThemeActivity : UnderActivity() {
  val textSizeTheme by lazy { getTextSizeTheme() }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(textSizeTheme)
    super.onCreate(savedInstanceState)
  }

  override fun onStart() {
    checkTheme()
    super.onStart()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.text_size, menu)
    return true
  }

  override fun onCreateContextMenu(menu: ContextMenu?, v: View?,
                                   menuInfo: ContextMenu.ContextMenuInfo?) {
    if(v?.id == R.id.option__text_size){
      val inflater = menuInflater
      inflater.inflate(R.menu.text_size__context_menu, menu)
    } else {
      super.onCreateContextMenu(menu, v, menuInfo)
    }
  }

  override fun onContextItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action__text__small -> trueRes { saveTextPreference(-1) }
      R.id.action__text__normal -> trueRes { saveTextPreference(0) }
      R.id.action__text__big -> trueRes { saveTextPreference(1) }
      else ->
        super.onContextItemSelected(item)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action__text__small -> trueRes { saveTextPreference(-1) }
      R.id.action__text__normal -> trueRes { saveTextPreference(0) }
      R.id.action__text__big -> trueRes { saveTextPreference(1) }
      else ->
        super.onOptionsItemSelected(item)
    }
  }

  fun checkTheme() {
    if (getTextSizeTheme() != textSizeTheme) {
      recreate()
    }
  }

  fun saveTextPreference(size: Int) {
    saveTextSize(size)
    checkTheme()
  }
}
