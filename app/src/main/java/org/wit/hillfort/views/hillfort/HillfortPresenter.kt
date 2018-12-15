package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.*

class HillfortPresenter(view: BaseView): BasePresenter(view)  {

  var map: GoogleMap? = null
  //var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
 // val locationRequest = createDefaultLocationRequest()

  var hillfort = HillfortModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var edit = false

  init {
    if (view.intent.hasExtra("placemark_edit")) {
      edit = true
      hillfort = view.intent.extras.getParcelable<HillfortModel>("placemark_edit")
      view.showPlacemark(hillfort)
    } else {
      if (checkLocationPermissions(view)){
        doSetCurrentLocation()
      }
    }
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(hillfort.lat, hillfort.lng)
  }

  fun locationUpdate(lat: Double, lng: Double) {
    hillfort.lat = lat
    hillfort.lng = lng
    hillfort.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.lat, hillfort.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.lat, hillfort.lng), hillfort.zoom))
    view?.showPlacemark(hillfort)
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    /*var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(l.latitude, l.longitude)
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }*/
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    /*locationService.lastLocation.addOnSuccessListener {
      locationUpdate(it.latitude, it.longitude)
    }*/
  }

  fun doAddOrSave(title: String, description: String) {
    hillfort.title = title
    hillfort.description = description
    /*async(UI){
      if (edit) {
        app.placemarks.update(placemark)
      } else {
        app.placemarks.create(placemark)
      }
      view?.finish()
    }*/
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    /*async(UI) {
      app.placemarks.delete(placemark)
      view?.finish()
    }*/
  }

  fun doSelectImage() {
    showImagePicker(view!!, IMAGE_REQUEST)
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        hillfort.images.add(data.data.toString())
        view?.showPlacemark(hillfort)
      }
      LOCATION_REQUEST -> {
        val location = data.extras.getParcelable<Location>("location")
        hillfort.lat = location.lat
        hillfort.lng = location.lng
        hillfort.zoom = location.zoom
        locationUpdate(hillfort.lat, hillfort.lng)
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

}