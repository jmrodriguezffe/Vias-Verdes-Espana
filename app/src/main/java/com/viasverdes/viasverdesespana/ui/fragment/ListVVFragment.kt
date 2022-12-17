package com.viasverdes.viasverdesespana.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.underlegendz.corelegendz.vm.VMFragment
import com.viasverdes.viasverdesespana.R
import com.viasverdes.viasverdesespana.data.VVDatabase
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.databinding.FragmentListItinerariesBinding
import com.viasverdes.viasverdesespana.ui.activity.ItineraryActivity
import com.viasverdes.viasverdesespana.ui.adapter.ListVVAdapter
import com.viasverdes.viasverdesespana.utils.*


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
  private lateinit var binding: FragmentListItinerariesBinding

  override fun initializeView() {
    binding = FragmentListItinerariesBinding.bind(view!!)
    context?.let {ctx ->
      binding.itinerariesList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(ctx)
      VVDatabase.getInstance(ctx)?.itineraryDAO()?.getAllLiveData()?.observe(this, this)

      binding.itinerariesInputSearch.setOnEditorActionListener { _, actionId, _ ->
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
        binding.itinerariesInputCa.adapter = adapter
        binding.itinerariesInputCa.setSelection(0)
      }
    }

    binding.itinerariesInputCa.onItemSelectedListener = this
    binding.itinerariesBtnSearch.setOnClickListener { search() }
  }

  private fun search() {
    val ca = if (binding.itinerariesInputCa.selectedItemPosition != 0) {
      binding.itinerariesInputCa.selectedItem.toString()
    }else{
      null
    }
    val province = if (binding.itinerariesInputProvinces.selectedItemPosition != 0) {
      binding.itinerariesInputProvinces.selectedItem.toString()
    }else{
      null
    }

    listVVAdapter?.filter(binding.itinerariesInputSearch.text.toString(), ca, province)
    binding.itinerariesLabelNoSearchFound.setVisible(listVVAdapter?.itemCount == 0)
  }

  override fun getLayoutResource(): Int {
    return R.layout.fragment__list_itineraries
  }

  override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
  ) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.list_itineraries, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action__search -> trueRes { toogleSearchContainer() }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun toogleSearchContainer() {
    binding.itinerariesContainerSearch.toogleVisibility()
    binding.itinerariesShadowSearch.visibility = binding.itinerariesContainerSearch.visibility
  }

  override fun onChanged(data: List<ItineraryBO>?) {
    if (data != null) {
      listVVAdapter = ListVVAdapter(data)
      listVVAdapter?.listener = this
      binding.itinerariesList.adapter = listVVAdapter
      binding.itinerariesLabelNoSearchFound.setVisible(listVVAdapter?.itemCount == 0)
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
    context?.let { ctx ->
      ArrayAdapter.createFromResource(
            ctx,
            getProvinceFromCA(position),
            android.R.layout.simple_spinner_item
      ).also { adapter ->
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding.itinerariesInputProvinces.adapter = adapter
      }
    }
  }

}