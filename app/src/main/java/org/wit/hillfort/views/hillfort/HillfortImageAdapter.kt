package org.wit.hillfort.views.hillfort

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import java.io.File

class HillfortImageAdapter constructor(private val images: List<String>)
  : androidx.recyclerview.widget.RecyclerView.Adapter<HillfortImageAdapter.ImageHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
    return ImageHolder(LayoutInflater.from(parent?.context).inflate(R.layout.image_hillfort, parent, false))
  }

  override fun onBindViewHolder(holder: ImageHolder, position: Int) {
    val image = images[position]
    holder.bind(image)
  }

  override fun getItemCount(): Int = images.size

  class ImageHolder constructor(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), AnkoLogger{

    fun bind(image: String){
      Glide.with(itemView.context).load(image).into(itemView.hillfortImage)
    }
  }
}