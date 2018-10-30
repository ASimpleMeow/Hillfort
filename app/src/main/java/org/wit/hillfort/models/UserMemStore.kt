package org.wit.hillfort.models

var lastUserId: Long = 0

internal fun getUserId(): Long = lastUserId++

class UserMemStore: UserStore {

  val users = ArrayList<UserModel>()

  override fun findAll(): MutableList<UserModel> {
    return users
  }

  override fun create(user: UserModel) {
    user.id = getId()
    users.add(user)
  }

  override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { u -> u.id == user.id }
    if (foundUser != null) {
      foundUser.name = user.name
      foundUser.email = user.email
      foundUser.phone = user.phone
      foundUser.passwordHash = user.passwordHash
      foundUser.hillforts = ArrayList(user.hillforts)
    }
  }

  override fun delete(user: UserModel) {
    users.remove(user)
  }

}