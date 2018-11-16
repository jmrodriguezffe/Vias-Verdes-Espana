package com.viasverdes.viasverdesespana.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.underlegendz.corelegendz.utils.ResourcesUtils
import com.viasverdes.viasverdesespana.*
import com.viasverdes.viasverdesespana.data.bo.ItineraryBO
import com.viasverdes.viasverdesespana.utils.AdapterClickListener
import com.viasverdes.viasverdesespana.utils.getImageResource
import com.viasverdes.viasverdesespana.utils.setVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row__itinerary.*
import java.util.*

class ListVVAdapter(private var data: List<ItineraryBO>) : RecyclerView.Adapter<ListVVAdapter.ListVVHolder>() {
  var listener: AdapterClickListener<ItineraryBO>? = null
  private var filterText: String? = null
  private var filterCA: String? = null
  private var filterProvince: String? = null
  private val backupData: List<ItineraryBO>

  init {
    backupData = data
  }

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
              || itineraryBO.name.contains(filterText!!, true)
              || itineraryBO.provinces.contains(filterText!!, true)
              || itineraryBO.ca.contains(filterText!!, true)
        val filterCASucces = filterCA.isNullOrEmpty()
              || itineraryBO.ca.contains(filterCA!!, true)
        val filterProvinceSucces = filterProvince.isNullOrEmpty()
              || itineraryBO.provinces.contains(filterProvince!!, true)

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

  class ListVVHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(itinerary: ItineraryBO) {
      itinerary__row__title.text = itinerary.name
      itinerary__row__provinces.text = itinerary.provinces
      itinerary__row__length.text = ResourcesUtils.getString(R.string.km, itinerary.length)
      itinerary__row__ccaa.text = itinerary.ca
      itinerary__row__walk_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WALK))
      itinerary__row__bicycle_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__BICYCLE))
      itinerary__row__wheelchair_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__WHEELCHAIR))
      itinerary__row__roller_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__ROLLER))
      itinerary__row__horse_user_type.setVisible(itinerary.userTypes.contains(USER_TYPE__HORSE))
      var imageResource = getImageResource(itinerary)
      if (imageResource == 0) {
        imageResource = R.drawable.ic__itinerary__no_image
      }
      itinerary__row__image.setImageResource(imageResource)
    }
  }
}