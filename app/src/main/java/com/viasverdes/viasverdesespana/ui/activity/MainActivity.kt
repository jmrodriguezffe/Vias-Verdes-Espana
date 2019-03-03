package com.viasverdes.viasverdesespana.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.underlegendz.underactivity.ActivityBuilder
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.fragment.InfoFragment
import com.viasverdes.viasverdesespana.ui.fragment.ListVVFragment
import com.viasverdes.viasverdesespana.ui.fragment.MapFragment
import com.viasverdes.viasverdesespana.ui.fragment.ToDoFragment
import com.viasverdes.viasverdesespana.utils.removeShiftMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : TextSizeThemeActivity() {

  val tagListVV = "LIST_VV"
  val tagMap = "MAP"
  val tagMoreInfo = "MORE_INFO"
  val tagSettings = "SETTINGS"
  val sectionSelected = "SECTION_SELECTED"
  var lastSectionSelected: Int = 0

  override fun configureActivityBuilder(builder: ActivityBuilder): ActivityBuilder {
    return builder.setContentLayout(R.layout.activity_main)
          .enableToolbar(true)
          .setToolbar(R.layout.toolbar)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    main__bottom_navigation.removeShiftMode()
    main__bottom_navigation.setOnNavigationItemSelectedListener { item ->
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
      main__bottom_navigation.selectedItemId = lastSectionSelected
    } else {
      main__bottom_navigation.selectedItemId = R.id.action__vv_list
    }

    toolbar.title = ""
    toolbar__title.setText(R.string.app_name)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    outState?.putInt(sectionSelected, lastSectionSelected)
  }

  protected fun getOrCreateListVV(): Fragment {
    var fragment: Fragment? = getFragment(tagListVV)
    if (fragment != null) {
      return fragment
    } else {
      return ListVVFragment.newInstance()
    }
  }

  protected fun getOrCreateMap(): Fragment {
    var fragment: Fragment? = getFragment(tagMap)
    if (fragment != null) {
      return fragment
    } else {
      return MapFragment.newInstance(null)
    }
  }

  protected fun getOrCreateMoreInfo(): Fragment {
    var fragment: Fragment? = getFragment(tagMoreInfo)
    if (fragment != null) {
      return fragment
    } else {
      return InfoFragment.newInstance()
    }
  }

  protected fun getOrCreateSettings(): Fragment {
    var fragment: Fragment? = getFragment(tagSettings)
    if (fragment != null) {
      return fragment
    } else {
      return ToDoFragment.newInstance()
    }
  }
}
