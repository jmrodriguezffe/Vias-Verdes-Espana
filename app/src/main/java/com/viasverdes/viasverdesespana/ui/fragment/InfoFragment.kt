package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.ui.adapter.MoreInfoPageAdapter
import kotlinx.android.synthetic.main.fragment__more_info.*


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
    more_info__container__viewpager.adapter = MoreInfoPageAdapter(childFragmentManager)
    more_info__list__tablayout.setupWithViewPager(more_info__container__viewpager)
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__more_info
  }

}