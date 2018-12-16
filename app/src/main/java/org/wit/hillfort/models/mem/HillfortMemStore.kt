package org.wit.hillfort.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

var lastId = 0L

internal fun getId():Long{
  return lastId++
}

class HillfortMemStore: HillfortStore, AnkoLogger{

  val hillforts = ArrayList<HillfortModel>()

  override suspend fun findAll():MutableList<HillfortModel>{
    return hillforts
  }

  override suspend fun findById(id:Long) : HillfortModel? {
    return hillforts.find { it.id == id }
  }

  override suspend fun create(hillfort: HillfortModel){
    hillfort.id = getId()
    hillforts.add(hillfort)
    logAll();
  }

  override suspend fun update(hillfort: HillfortModel) {
    var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id}
    if (foundHillfort != null){
      foundHillfort.title = hillfort.title
      foundHillfort.description = hillfort.description
      foundHillfort.image = hillfort.image
      foundHillfort.location = hillfort.location
      logAll()
    }
  }

  override suspend fun delete(hillfort: HillfortModel) {
    hillforts.remove(hillfort)
  }

  override fun clear() {
    hillforts.clear()
  }

  fun logAll(){
    hillforts.forEach { info("${it}") }
  }
}