package org.wit.hillfort.models

interface UserStore {
  fun findAll(): MutableList<UserModel>
  fun create(user: UserModel)
  fun update(user: UserModel)
  fun delete(user: UserModel)
}