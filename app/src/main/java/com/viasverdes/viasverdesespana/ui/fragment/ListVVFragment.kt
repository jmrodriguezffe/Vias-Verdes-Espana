package com.viasverdes.viasverdesespana.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.ui.activity.ItineraryActivity
import com.viasverdes.viasverdesespana.ui.adapter.ListVVAdapter
import com.viasverdes.viasverdesespana.utils.AdapterClickListener
import com.viasverdes.viasverdesespana.utils.getProvinceFromCA
import com.viasverdes.viasverdesespana.utils.toogleVisibility
import com.viasverdes.viasverdesespana.utils.trueRes
import kotlinx.android.synthetic.main.fragment__list_itineraries.*


class ListVVFragment : VMFragment(), Observer<List<ItineraryBO>>, AdapterClickListener<ItineraryBO>, AdapterView.OnItemSelectedListener {

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
    context?.let {ctx ->
      itineraries__list.layoutManager = LinearLayoutManager(ctx)
      VVDatabase.getInstance(ctx)?.itineraryDAO()?.getAllLiveData()?.observe(this, this)

      itineraries__input__search.setOnEditorActionListener { _, actionId, _ ->
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
          search()
          true
        }else{
          false
        }
      }

      ArrayAdapter.createFromResource(
            ctx,
            R.array.ca_array,
            android.R.layout.simple_spinner_item
      ).also { adapter ->
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        itineraries__input__ca.adapter = adapter
        itineraries__input__ca.setSelection(0)
      }
    }

    itineraries__input__ca.onItemSelectedListener = this
    itineraries__btn__search.setOnClickListener { search() }
  }

  private fun search() {
    var ca = if(itineraries__input__ca.selectedItemPosition != 0){
      itineraries__input__ca.selectedItem.toString()
    }else{
      null
    }
    var province = if(itineraries__input__provinces.selectedItemPosition != 0){
      itineraries__input__provinces.selectedItem.toString()
    }else{
      null
    }

    listVVAdapter?.filter(itineraries__input__search.text.toString(), ca, province)
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

  override fun onNothingSelected(p0: AdapterView<*>?) {
    // Nothing to do
  }

  override fun onItemSelected(p0: AdapterView<*>?,
                              p1: View?,
                              position: Int,
                              p3: Long
  ) {
    ArrayAdapter.createFromResource(
          context,
          getProvinceFromCA(position),
          android.R.layout.simple_spinner_item
    ).also { adapter ->
      // Specify the layout to use when the list of choices appears
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      // Apply the adapter to the spinner
      itineraries__input__provinces.adapter = adapter
    }
  }

}