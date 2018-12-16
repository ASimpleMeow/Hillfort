package org.wit.hillfort.models.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortStoreRoom(val context: Context) : HillfortStore {

  var dao: HillfortDao

  init {
    val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.hillfortDao()
  }

  override suspend fun findAll(): MutableList<HillfortModel> {
    val deferredHillforts = bg{
      dao.findAll()
    }
    val hillforts = deferredHillforts.await()
    return hillforts
  }

  override suspend fun findById(id: Long): HillfortModel? {
    val deferredHillforts = bg{
      dao.findById(id)
    }
    val hillforts = deferredHillforts.await()
    return hillforts
  }

  override suspend fun create(hillfort: HillfortModel) {
    bg{
      dao.create(hillfort)
    }
  }

  override suspend fun update(hillfort: HillfortModel) {
    bg{
      dao.update(hillfort)
    }
  }

  override suspend fun delete(hillfort: HillfortModel) {
    bg{
      dao.deleteHillfort(hillfort)
    }
  }

  override fun clear() {
  }
}