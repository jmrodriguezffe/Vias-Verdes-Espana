package com.viasverdes.viasverdesespana.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.viasverdes.viasverdesespana.*
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.databinding.RowItineraryBinding
import com.viasverdes.viasverdesespana.utils.*
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class ListVVAdapter(private var data: List<ItineraryBO>) : androidx.recyclerview.widget.RecyclerView.Adapter<ListVVAdapter.ListVVHolder>() {
  var listener: AdapterClickListener<ItineraryBO>? = null
  private var filterText: String? = null
  private var filterCA: String? = null
  private var filterProvince: String? = null
  private val backupData: List<ItineraryBO> = data

  fun filter(filterText: String?,
             filterCA: String?,
             filterProvince: String?
  ) {
    this.filterText = filterText
    this.filterCA = filterCA
    this.filterProvince = filterProvince
    applyFilter()
  }

  private fun applyFilter() {
    if (filterText.isNullOrEmpty() && filterCA.isNullOrEmpty() && filterProvince.isNullOrEmpty()) {
      data = backupData
    } else {
      val list = LinkedList<ItineraryBO>()
      for (itineraryBO in backupData) {
        val filterTextSucces = filterText.isNullOrEmpty()
              || itineraryBO.name.search(filterText!!)
              || itineraryBO.provinces.search(filterText!!)
              || itineraryBO.ca.search(filterText!!)
        val filterCASucces = filterCA.isNullOrEmpty()
              || itineraryBO.ca.search(filterCA!!)
        val filterProvinceSucces = filterProvince.isNullOrEmpty()
              || itineraryBO.provinces.search(filterProvince!!)

        if (filterTextSucces && filterCASucces && filterProvinceSucces) {
          list.add(itineraryBO)
        }
      }
      data = list
    }
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
  ): ListVVHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.row__itinerary, parent, false)
    return ListVVHolder(view)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onBindViewHolder(
        holder: ListVVHolder,
        position: Int
  ) {
    val itinerary = data[position]
    holder.bind(itinerary)
    holder.itemView.setOnClickListener { listener?.onItemClick(itinerary) }
  }

  class ListVVHolder(override val containerView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
    val binding: RowItineraryBinding = RowItineraryBinding.bind(containerView)

    fun bind(itinerary: ItineraryBO) {
      binding.itineraryRowTitle.text = itinerary.name
      binding.itineraryRowProvinces.text = itinerary.provinces
      binding.itineraryRowLength.text = ResourcesUtils.getString(R.string.km, itinerary.length)
      binding.itineraryRowCcaa.text = itinerary.ca
      binding.itineraryRowWalkUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__WALK))
      binding.itineraryRowBicycleUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__BICYCLE))
      binding.itineraryRowWheelchairUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__WHEELCHAIR))
      binding.itineraryRowRollerUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__ROLLER))
      binding.itineraryRowHorseUserType.setVisible(itinerary.userTypes.contains(USER_TYPE__HORSE))

      loadImage(binding.itineraryRowImage, getRemoteImageThumbUri(itinerary), getRemoteImageThumbUri(itinerary, true))
    }
  }
}