package org.wit.hillfort.models

interface HillfortStore {
  suspend fun findAll(): MutableList<HillfortModel>
  suspend fun findById(id:Long) : HillfortModel?
  suspend fun create(hillfort: HillfortModel)
  suspend fun update(hillfort: HillfortModel)
  suspend fun delete(hillfort: HillfortModel)
  fun clear()
}