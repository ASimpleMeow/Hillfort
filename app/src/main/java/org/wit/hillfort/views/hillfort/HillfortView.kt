package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView


class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  lateinit var map: GoogleMap
  var shareActionProvider: ShareActionProvider? = null
  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    init(toolbarAdd, true)

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

    chooseImage.setOnClickListener { presenter.doSelectImage() }

    val layoutManager = GridLayoutManager(this,2)
    hillfortImageGallery.layoutManager = layoutManager
    presenter.loadHillfortImages()
  }

  override fun showHillfortImages(images: List<String>) {
    hillfortImageGallery.adapter = HillfortImageAdapter(images)
    hillfortImageGallery.adapter?.notifyDataSetChanged()
  }

  override fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
    showHillfortImages(hillfort.images)
    lat.setText("%.6f".format(hillfort.location.lat))
    lng.setText("%.6f".format(hillfort.location.lng))
    notes.setText(hillfort.notes)
    visit.isChecked = hillfort.visited
    date_visited.updateDate(hillfort.yearVisited, hillfort.monthVisited, hillfort.dayVisited)
    rating.rating = hillfort.rating
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    if (presenter.edit) menu.getItem(0).isVisible = true
    shareActionProvider = MenuItemCompat.getActionProvider(menu.findItem(R.id.item_share)) as ShareActionProvider
    shareActionProvider?.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
    shareActionProvider?.setShareIntent(presenter.createShareIntent())
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        if (hillfortTitle.text.toString().isEmpty()) toast(R.string.hint_hillfortTitle)
        else presenter.doAddOrSave(hillfortTitle.text.toString(),
            description.text.toString(),
            notes.text.toString(),
            visit.isChecked,
            date_visited,
            rating.rating)
      }
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_cancel -> {
        presenter.doCancel()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}
