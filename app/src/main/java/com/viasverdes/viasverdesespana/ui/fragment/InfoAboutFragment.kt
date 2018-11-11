package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import kotlinx.android.synthetic.main.fragment__more_info__about.*


class InfoAboutFragment : VMFragment() {

  companion object {

    fun newInstance(): InfoAboutFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = InfoAboutFragment()
      fragment.arguments = args
      return fragment
    }
  }


  override fun initializeView() {
    more_info__about__webview.loadUrl("file:///android_asset/about.html")
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__more_info__about
  }

}