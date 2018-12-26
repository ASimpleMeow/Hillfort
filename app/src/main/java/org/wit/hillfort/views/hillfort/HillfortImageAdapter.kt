package org.wit.hillfort.views.hillfort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.wit.hillfort.R

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

  class ImageHolder constructor(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

    fun bind(image: String){
      var imageView: ImageView = itemView.findViewById(R.id.hillfortImage)
      Picasso.get()
          .load(image)
          .fit()
          .into(imageView)
    }
  }
}