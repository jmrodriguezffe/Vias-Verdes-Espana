package com.viasverdes.viasverdesespana.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.ui.activity.ItineraryActivity
import com.viasverdes.viasverdesespana.ui.activity.MapActivity
import com.viasverdes.viasverdesespana.ui.adapter.ListVVAdapter
import com.viasverdes.viasverdesespana.utils.AdapterClickListener
import kotlinx.android.synthetic.main.fragment__list_itineraries.*

class ListVVFragment : VMFragment(), Observer<List<ItineraryBO>>, AdapterClickListener<ItineraryBO> {

  companion object {
    fun newInstance(): ListVVFragment {
      val args: Bundle = Bundle()
//            args.putSerializable(ARG_CAUGHT, caught)
      val fragment = ListVVFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun initializeView() {
    context?.let {
      itineraries__list.layoutManager = LinearLayoutManager(context)
      VVDatabase.getInstance(it)?.itineraryDAO()?.getAllLiveData()?.observe(this, this)
    }
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__list_itineraries
  }

  override fun onChanged(data: List<ItineraryBO>?) {
    if (data != null) {
      val listVVAdapter = ListVVAdapter(data)
      listVVAdapter.listener = this
      itineraries__list.adapter = listVVAdapter
    }
  }

  override fun onItemClick(item: ItineraryBO) {
    activity?.let { ItineraryActivity.start(it, item) }
  }

}