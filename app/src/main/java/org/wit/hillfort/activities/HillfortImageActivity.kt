package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R

class HillfortImageActivity: AppCompatActivity(), AnkoLogger {

  lateinit var originalImage: String
  lateinit var imageView: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_image)

    imageView = findViewById(R.id.fullHillfortImage)
    originalImage = intent.getStringExtra("image")

    info("Opening image ${originalImage}")

    displayImage(originalImage)
  }

  private fun displayImage(image: String){
    Picasso.get()
        .load(image)
        .fit()
        .into(imageView)
  }
}