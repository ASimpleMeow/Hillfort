package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.DatePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.helpers.*
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.*
import org.wit.hillfort.views.editlocation.EditLocationView
import java.util.ArrayList

class HillfortPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

  var map: GoogleMap? = null
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  var hillfort = HillfortModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var edit = false

  init {
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
      view.showHillfort(hillfort)
    } else {
      if (checkLocationPermissions(view)){
        doSetCurrentLocation()
      }
    }
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(hillfort.location.lat, hillfort.location.lng)
  }

  fun locationUpdate(lat: Double, lng: Double) {
    hillfort.location.lat = lat
    hillfort.location.lng = lng
    hillfort.location.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
    view?.showHillfort(hillfort)
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(l.latitude, l.longitude)
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(it.latitude, it.longitude)
    }
  }

  fun doAddOrSave(title: String, description: String, notes: String, visited: Boolean, datePicker: DatePicker, rating: Float) {
    hillfort.title = title
    hillfort.description = description
    hillfort.notes = notes
    hillfort.visited = visited
    hillfort.dayVisited = datePicker.dayOfMonth
    hillfort.monthVisited = datePicker.month
    hillfort.yearVisited = datePicker.year
    hillfort.rating = rating
    async(UI){
      if (edit) {
        app.hillforts.update(hillfort)
      } else {
        app.hillforts.create(hillfort)
      }
      view?.finish()
    }
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    async(UI) {
      app.hillforts.delete(hillfort)
      view?.finish()
    }
  }

  fun doSelectImage() {
    showImagePicker(view!!, IMAGE_REQUEST)
  }

  fun takeImage(){
    showCamera(view!!, CAMERA_REQUEST)
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      CAMERA_REQUEST -> {
        if (CAMERA_IMAGE_URI.isNullOrBlank()) return
        if (!hillfort.images.contains(CAMERA_IMAGE_URI)) hillfort.images.add(CAMERA_IMAGE_URI!!)
        view?.showHillfort(hillfort)
      }
      IMAGE_REQUEST -> {
        if (data == null) return
        if (!hillfort.images.contains(data.data.toString())) hillfort.images.add(data.data.toString())
        view?.showHillfort(hillfort)
      }
      LOCATION_REQUEST -> {
        if (data == null) return
        val location = data.extras.getParcelable<Location>("location")
        hillfort.location.lat = location.lat
        hillfort.location.lng = location.lng
        hillfort.location.zoom = location.zoom
        locationUpdate(hillfort.location.lat, hillfort.location.lng)
      }
    }
  }

  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(defaultLocation.lat, defaultLocation.lng)
    }
  }

  fun loadHillfortImages() {
    view?.showHillfortImages(hillfort.images)
  }

  fun createShareIntent(): Intent {
    val text = "Hillfort: ${hillfort.title}, Description: ${hillfort.description}, Location: ${LatLng(hillfort.location.lat, hillfort.location.lng)}"
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, text)
    return shareIntent
  }
}