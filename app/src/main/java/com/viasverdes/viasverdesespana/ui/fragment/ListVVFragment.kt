package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R

class ListVVFragment : VMFragment() {

    companion object{

        fun newInstance():ListVVFragment{
            val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
            val fragment = ListVVFragment()
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