package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId: Long = 0

internal fun getId(): Long = lastId++

class HillfortMemStore: HillfortStore, AnkoLogger {

  val hillforts = ArrayList<HillfortModel>()

  override fun findAll(): MutableList<HillfortModel> {
    return hillforts
  }

  override fun create(hillfort: HillfortModel) {
    hillfort.id = getId()
    hillforts.add(hillfort)
  }

  override fun update(hillfort: HillfortModel) {
    var foundHillfort: HillfortModel? = hillforts.find { h -> h.id == hillfort.id }
    if (foundHillfort != null) {
      foundHillfort.title = hillfort.title
      foundHillfort.description = hillfort.description
      foundHillfort.images = ArrayList(hillfort.images)
      foundHillfort.lat = hillfort.lat
      foundHillfort.lng = hillfort.lng
      foundHillfort.zoom = hillfort.zoom
      foundHillfort.visited = hillfort.visited
      foundHillfort.notes = hillfort.notes
      foundHillfort.dayVisited = hillfort.dayVisited
      foundHillfort.monthVisited = hillfort.monthVisited
      foundHillfort.yearVisited = hillfort.yearVisited
      logAll()
    }
  }

  override fun delete(hillfort: HillfortModel) {
    hillforts.remove(hillfort)
  }

  fun logAll(){
    hillforts.forEach { info("${it}") }
  }
}