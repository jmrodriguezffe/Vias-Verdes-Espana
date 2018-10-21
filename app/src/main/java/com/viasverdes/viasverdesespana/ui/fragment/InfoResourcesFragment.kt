package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R

class InfoResourcesFragment : VMFragment() {

  companion object {

    fun newInstance(): InfoResourcesFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = InfoResourcesFragment()
      fragment.arguments = args
      return fragment
    }
  }


  override fun initializeView() {

  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__more_info__resources
  }

}