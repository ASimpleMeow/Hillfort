package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*

class MainApp : Application(), AnkoLogger {

  lateinit var hillforts: HillfortStore
  lateinit var users: UserStore

  override fun onCreate() {
    super.onCreate()

    var initialHillforts = ArrayList<HillfortModel>()
    initialHillforts.add(HillfortModel(title = "IR0905 Ballynamona Lower, Waterford",
        description = "A small sub-rectangular area barely projecting E from the mainland at an altitude of 28m OD.",
        lat = 51.996723,
        lng = -7.583626,
        zoom = 15f))

    users = UserMemStore()
    hillforts = HillfortJSONStore(applicationContext, initialHillforts)
    info("Hillfort started")
  }
}