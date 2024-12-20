package com.viasverdes.viasverdesespana.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.underlegendz.underactivity.ActivityBuilder
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.fragment.InfoFragment
import com.viasverdes.viasverdesespana.ui.fragment.ListVVFragment
import com.viasverdes.viasverdesespana.ui.fragment.MapFragment
import com.viasverdes.viasverdesespana.utils.removeShiftMode

class MainActivity : TextSizeThemeActivity() {

  val tagListVV = "LIST_VV"
  val tagMap = "MAP"
  val tagMoreInfo = "MORE_INFO"
  val sectionSelected = "SECTION_SELECTED"
  var lastSectionSelected: Int = 0

  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder.setContentLayout(R.layout.activity_main)
          .enableToolbar(true)
          .setToolbar(R.layout.toolbar)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main__bottom_navigation)
    bottomNavigationView.removeShiftMode()
    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
      if (lastSectionSelected != item.itemId) {
        when (item.itemId) {
          R.id.action__vv_list -> {
            setFragment(getOrCreateListVV(), tagListVV, lastSectionSelected != 0)
          }
          R.id.action__map -> {
            setFragment(getOrCreateMap(), tagMap, lastSectionSelected != 0)
          }
          R.id.action__more_info -> {
            setFragment(getOrCreateMoreInfo(), tagMoreInfo, lastSectionSelected != 0)
          }
          else -> return@setOnNavigationItemSelectedListener false
        }
        lastSectionSelected = item.itemId
      }
      return@setOnNavigationItemSelectedListener true
    }

    if (savedInstanceState != null && savedInstanceState.containsKey(sectionSelected)) {
      lastSectionSelected = savedInstanceState.getInt(sectionSelected)
      bottomNavigationView.selectedItemId = lastSectionSelected
    } else {
      bottomNavigationView.selectedItemId = R.id.action__vv_list
    }

    toolbar.title = ""
    findViewById<TextView>(R.id.toolbar__title).setText(R.string.app_name)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(sectionSelected, lastSectionSelected)
  }

  private fun getOrCreateListVV(): Fragment {
    return getFragment(tagListVV) ?: ListVVFragment.newInstance()
  }

  private fun getOrCreateMap(): Fragment {
    return getFragment(tagMap) ?: MapFragment.newInstance(null)
  }

  private fun getOrCreateMoreInfo(): Fragment {
    return getFragment(tagMoreInfo) ?: InfoFragment.newInstance()
  }
}
