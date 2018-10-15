package com.viasverdes.viasverdesespana.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.ui.activity.ItineraryActivity
import com.viasverdes.viasverdesespana.ui.adapter.ListVVAdapter
import com.viasverdes.viasverdesespana.utils.AdapterClickListener
import com.viasverdes.viasverdesespana.utils.toogleVisibility
import com.viasverdes.viasverdesespana.utils.trueRes
import kotlinx.android.synthetic.main.fragment__list_itineraries.*
import android.widget.ArrayAdapter
import com.viasverdes.viasverdesespana.autocompleteSearchValues


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

  private var listVVAdapter: ListVVAdapter? = null

  override fun initializeView() {
    context?.let {
      itineraries__list.layoutManager = LinearLayoutManager(context)
      VVDatabase.getInstance(it)?.itineraryDAO()?.getAllLiveData()?.observe(this, this)

      val adapter = ArrayAdapter<String>(context, android.R.layout.select_dialog_item, autocompleteSearchValues())
      itineraries__input__search.setAdapter(adapter)
      itineraries__input__search.threshold = 2
    }

    itineraries__btn__search.setOnClickListener { search() }
  }

  private fun search() {
    listVVAdapter?.filter(itineraries__input__search.text.toString())
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__list_itineraries
  }

  override fun onCreateOptionsMenu(
        menu: Menu?,
        inflater: MenuInflater?
  ) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.list_itineraries, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action__search -> trueRes { toogleSearchContainer() }
//      R.id.action__filter -> toogleFilterContainer()
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun toogleSearchContainer() {
    itineraries__container__search.toogleVisibility()
    itineraries__shadow__search.visibility = itineraries__container__search.visibility
  }

  override fun onChanged(data: List<ItineraryBO>?) {
    if (data != null) {
      listVVAdapter = ListVVAdapter(data)
      listVVAdapter?.listener = this
      itineraries__list.adapter = listVVAdapter
    }
  }

  override fun onItemClick(item: ItineraryBO) {
    activity?.let { ItineraryActivity.start(it, item) }
  }

}