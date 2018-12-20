package org.wit.hillfort.views.hillfortlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
  fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                   private val listener: HillfortListener) : androidx.recyclerview.widget.RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val hillfort = hillforts[holder.adapterPosition]
    holder.bind(hillfort, listener)
  }

  override fun getItemCount(): Int = hillforts.size

  class MainHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    fun bind(hillfort: HillfortModel,  listener : HillfortListener) {
      itemView.hillfortTitle.text = hillfort.title
      itemView.description.text = hillfort.description
      if (hillfort.images.isNotEmpty()) Glide.with(itemView.context).load(hillfort.images.first()).into(itemView.imageIcon)
      itemView.visited_icon.setOnClickListener {
        hillfort.visited = !hillfort.visited
        showIcons(hillfort, itemView.fav_icon, itemView.visited_icon)
      }
      itemView.fav_icon.setOnClickListener {
        hillfort.favorite = !hillfort.favorite
        showIcons(hillfort, itemView.fav_icon, itemView.visited_icon)
      }
      showIcons(hillfort, itemView.fav_icon, itemView.visited_icon)
      itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
    }

    private fun showIcons(hillfort: HillfortModel, favIcon: ImageView, visitedIcon: ImageView){
      if (hillfort.favorite) favIcon.setImageResource(R.drawable.ic_favorite_black_24dp)
      else favIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp)

      if (hillfort.visited) visitedIcon.setImageResource(R.drawable.ic_visibility_black_24dp)
      else visitedIcon.setImageResource(R.drawable.ic_visibility_off_black_24dp)
    }
  }
}