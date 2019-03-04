package com.viasverdes.viasverdesespana.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.fragment.InfoAboutFragment
import com.viasverdes.viasverdesespana.ui.fragment.InfoLegalTextFragment
import com.viasverdes.viasverdesespana.ui.fragment.InfoResourcesFragment

class MoreInfoPageAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when (position) {
      0 -> InfoResourcesFragment.newInstance()
      1 -> InfoAboutFragment.newInstance()
      2 -> InfoLegalTextFragment.newInstance()
      else -> InfoResourcesFragment.newInstance()
    }
  }

  override fun getCount(): Int {
    return 3
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return when (position) {
      0 -> ResourcesUtils.getString(R.string.section__more_info__resources)
      1 -> ResourcesUtils.getString(R.string.section__more_info__about)
      2 -> ResourcesUtils.getString(R.string.section__more_info__legal_text)
      else -> ResourcesUtils.getString(R.string.section__more_info__resources)
    }
  }
}