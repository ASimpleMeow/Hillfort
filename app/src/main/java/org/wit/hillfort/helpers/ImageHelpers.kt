package org.wit.hillfort.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import org.wit.hillfort.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

var CAMERA_IMAGE_URI: String? = null

fun showImagePicker(parent: Activity, id: Int) {
  val intent = Intent()
  intent.type = "image/*"
  intent.action = Intent.ACTION_OPEN_DOCUMENT
  intent.addCategory(Intent.CATEGORY_OPENABLE)
  val chooser = Intent.createChooser(intent, R.string.button_changeImage.toString())
  parent.startActivityForResult(chooser, id)
}

fun showCamera(parent: Activity, id: Int){
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  if (intent.resolveActivity(parent.packageManager) == null) return
  val photoFile: File? = try {
    createImageFile(parent)
  } catch(ex: IOException){
    null
  }
  if (photoFile != null){
    val photoURI: Uri = FileProvider.getUriForFile(parent, "org.wit.hillfort.provider", photoFile)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    intent.putExtra("imageURI", photoURI)
    parent.startActivityForResult(intent, id)
  }
}

@Throws(IOException::class)
private fun createImageFile(parent: Activity): File{
  val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
  val storageDir: File = parent.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  return File.createTempFile(
      "JPEG_${timeStamp}_", /* prefix */
      ".jpg", /* suffix */
      storageDir /* directory */
  ).apply {
    CAMERA_IMAGE_URI = absolutePath
  }
}

fun readImage(activity: Activity, resultCode: Int, data: Intent?): Bitmap? {
  var bitmap: Bitmap? = null
  if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
    try {
      bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data.data)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  return bitmap
}

fun readImageFromPath(context: Context, path : String) : Bitmap? {
  var bitmap : Bitmap? = null
  val uri = Uri.parse(path)
  if (uri != null) {
    try {
      val parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")
      val fileDescriptor = parcelFileDescriptor.getFileDescriptor()
      bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
      parcelFileDescriptor.close()
    } catch (e: Exception) {
    }
  }
  return bitmap
}