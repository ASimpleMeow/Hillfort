package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortMemStore
import org.wit.hillfort.models.HillfortModel

class MainApp : Application(), AnkoLogger {

  val hillforts = HillfortMemStore()

  override fun onCreate() {
    super.onCreate()
    info("Hillfort started")
    hillforts.create(HillfortModel(title = "IR0905 Ballynamona Lower, Waterford",
        description = "A small sub-rectangular area barely projecting E from the mainland at an altitude of 28m OD.",
        image = "",
        lat = 51.996723,
        lng = -7.583626,
        zoom = 15f))
  }
}