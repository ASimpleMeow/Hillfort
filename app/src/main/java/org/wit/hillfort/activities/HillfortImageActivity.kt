package org.wit.hillfort.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hillfort_image.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.R.id.image_change
import org.wit.hillfort.R.id.image_delete
import org.wit.hillfort.helpers.showImagePicker

class HillfortImageActivity: AppCompatActivity(), AnkoLogger {

  val IMAGE_REQUEST = 1

  lateinit var original: String
  lateinit var image: String
  lateinit var imageView: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_image)

    imageView = findViewById(R.id.fullHillfortImage)
    image = intent.getStringExtra("image")
    original = image

    toolbarImage.title = title
    setSupportActionBar(toolbarImage)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    info("Opening image ${image_delete}")

    displayImage()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort_image, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when(item?.itemId){
      R.id.image_delete -> {
        image=""
        onBackPressed()
      }
      R.id.image_change -> {
        showImagePicker(this, IMAGE_REQUEST)
      }
      android.R.id.home -> onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when(requestCode){
      IMAGE_REQUEST -> {
        if (data != null){
          image = data.data!!.toString()
          displayImage()
        }
      }
    }
  }

  override fun onBackPressed() {
    val resultIntent = Intent()
    resultIntent.putExtra("image", image)
    resultIntent.putExtra("original", original)
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
    super.onBackPressed()
  }

  private fun displayImage(){
    Picasso.get()
        .load(image)
        .fit()
        .into(imageView)
  }
}