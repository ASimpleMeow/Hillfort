package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.exists
import org.wit.hillfort.helpers.read
import org.wit.hillfort.helpers.write
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*


val JSON_FILE = "hillfort.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long{
  return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger{

  val context: Context
  var hillforts = mutableListOf<HillfortModel>()

  constructor(context: Context){
    this.context = context
    if (exists(context, JSON_FILE)){
      deserialize()
    }
  }

  override suspend fun findAll(): MutableList<HillfortModel> {
    return hillforts
  }

  override suspend fun findById(id:Long) : HillfortModel? {
    return hillforts.find { it.id == id }
  }

  override suspend fun create(hillfort: HillfortModel) {
    hillfort.id = generateRandomId()
    hillforts.add(hillfort)
    serialize()
  }


  override suspend fun update(hillfort: HillfortModel) {
    var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id}
    if (foundHillfort != null){
      foundHillfort.title = hillfort.title
      foundHillfort.description = hillfort.description
      foundHillfort.images = ArrayList(hillfort.images)
      foundHillfort.visited = hillfort.visited
      foundHillfort.location = hillfort.location
      foundHillfort.visited = hillfort.visited
      foundHillfort.notes = hillfort.notes
      foundHillfort.dayVisited = hillfort.dayVisited
      foundHillfort.monthVisited = hillfort.monthVisited
      foundHillfort.yearVisited = hillfort.yearVisited
      foundHillfort.rating = hillfort.rating
      serialize()
    }
  }

  override suspend fun delete(hillfort: HillfortModel) {
    hillforts.remove(hillfort)
    serialize()
  }

  override fun clear() {
    hillforts.clear()
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(hillforts, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    hillforts = Gson().fromJson(jsonString, listType)
  }
}