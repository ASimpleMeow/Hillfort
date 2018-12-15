package org.wit.hillfort.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import java.math.RoundingMode
import java.text.DecimalFormat
import com.chauthai.swipereveallayout.ViewBinderHelper


interface HillfortListener {
  fun onHillfortClick(hillfort: HillfortModel)
  fun onHillfortMenuDeleteClick(hillfort: HillfortModel)
  fun onHillfortMenuVisitClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                  private val listener: HillfortListener): androidx.recyclerview.widget.RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

  private val viewBinderHelper = ViewBinderHelper()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val hillfort = hillforts[holder.adapterPosition]
    viewBinderHelper.bind(holder.itemView as SwipeRevealLayout, hillfort.id.toString())
    holder.bind(hillfort, listener)
  }

  override fun getItemCount(): Int = hillforts.size

  class MainHolder constructor(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

    fun bind(hillfort: HillfortModel, listener: HillfortListener){
      val df = DecimalFormat("#.###")
      df.roundingMode = RoundingMode.CEILING
      itemView.hillfortTitle.text = hillfort.title
      itemView.description.text = hillfort.description
      itemView.location.text = LatLng(df.format(hillfort.lat).toDouble(), df.format(hillfort.lng).toDouble()).toString()
      if (hillfort.images.isNotEmpty()) itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.images.first()))
      if (hillfort.visited) itemView.hillfort_card.setBackgroundResource(R.color.colorVisited)
      else itemView.hillfort_card.setBackgroundResource(R.color.colorNotVisited)
      itemView.hillfort_card_menu.btn_hillfort_delete.setOnClickListener { listener.onHillfortMenuDeleteClick(hillfort) }
      itemView.hillfort_card_menu.btn_hillfort_visited.setOnClickListener { listener.onHillfortMenuVisitClick(hillfort) }
      itemView.hillfort_card.setOnClickListener { listener.onHillfortClick(hillfort) }
    }
  }
}