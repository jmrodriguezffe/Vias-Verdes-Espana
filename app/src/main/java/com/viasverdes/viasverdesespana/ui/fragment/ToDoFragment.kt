package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R

class ToDoFragment : VMFragment() {

  companion object {

    fun newInstance(): ToDoFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = ToDoFragment()
      fragment.arguments = args
      return fragment
    }
  }


  override fun initializeView() {

  }

  override fun getLayoutResource(): Int {
    return R.layout.layout_working
  }

}