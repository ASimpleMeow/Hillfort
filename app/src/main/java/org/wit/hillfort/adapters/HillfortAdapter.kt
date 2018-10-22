package org.wit.hillfort.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import java.math.RoundingMode
import java.text.DecimalFormat

interface HillfortListener {
  fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                  private val listener: HillfortListener): RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val hillfort = hillforts[holder.adapterPosition]
    holder.bind(hillfort, listener)
  }

  override fun getItemCount(): Int = hillforts.size

  class MainHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){

    fun bind(hillfort: HillfortModel, listener: HillfortListener){
      val df = DecimalFormat("#.###")
      df.roundingMode = RoundingMode.CEILING
      itemView.hillfortTitle.text = hillfort.title
      itemView.description.text = hillfort.description
      itemView.location.text = LatLng(df.format(hillfort.lat).toDouble(), df.format(hillfort.lng).toDouble()).toString()
      if (hillfort.images.isNotEmpty())
        itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.images.first()))
      itemView.setOnClickListener{ listener.onHillfortClick(hillfort) }
    }
  }
}