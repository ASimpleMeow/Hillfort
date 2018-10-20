package org.wit.hillfort.models

interface HillfortStore {
  fun findAll(): MutableList<HillfortModel>
  fun create(hillfort: HillfortModel)
  fun update(hillfort: HillfortModel)
  fun delete(hillfort: HillfortModel)
}