package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.exists
import org.wit.hillfort.helpers.read
import org.wit.hillfort.helpers.write
import java.util.ArrayList

class UserJSONStore: UserStore, AnkoLogger {

  val JSON_USERS_FILE="users.json"
  val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
  val usersListType = object : TypeToken<ArrayList<UserModel>>() {}.type

  val context: Context
  var users = mutableListOf<UserModel>()

  constructor(context: Context, initialUsers: List<UserModel>, initialHillforts: List<HillfortModel>){
    this.context = context
    if (exists(context, JSON_USERS_FILE)){
      deserialize()
    } else {
      initialUsers.forEach {
        it.hillforts = ArrayList(initialHillforts)
        create(it.copy())
      }
    }
  }

  override fun findAll(): MutableList<UserModel> {
    return users
  }

  override fun create(user: UserModel) {
    user.id = generateRandomId()
    users.add(user)
    serialize()
  }

  override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { u -> u.id == user.id }
    if (foundUser != null){
      foundUser.name = user.name
      foundUser.email = user.email
      foundUser.phone = user.phone
      foundUser.passwordHash = user.passwordHash
      foundUser.hillforts = ArrayList(user.hillforts)
      serialize()
      logAll()
    }
  }

  override fun delete(user: UserModel) {
    users.remove(user)
    serialize()
  }

  private fun serialize(){
    val jsonString = gsonBuilder.toJson(users, usersListType)
    info(jsonString)
    write(context, JSON_USERS_FILE, jsonString)
  }

  private fun deserialize(){
    val jsonString = read(context, JSON_USERS_FILE)
    users = Gson().fromJson(jsonString, usersListType)
  }

  private fun logAll(){
    users.forEach{ info("${it}") }
  }
}