package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.adapter.MoreInfoPageAdapter


class InfoFragment : VMFragment() {

  companion object {
    fun newInstance(): InfoFragment {
      val args: Bundle = Bundle()
      val fragment = InfoFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun initializeView() {
    view?.findViewById<ViewPager>(R.id.more_info__container__viewpager)?.let {
      it.adapter = MoreInfoPageAdapter(childFragmentManager)
      view?.findViewById<TabLayout>(R.id.more_info__list__tablayout)?.setupWithViewPager(it)
    }
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__more_info
  }

}