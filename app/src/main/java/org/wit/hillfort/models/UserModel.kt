package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                     var name: String ="",
                     var email: String="",
                     var phone: String="",
                     var passwordHash: String="",
                     var hillforts: MutableList<HillfortModel> = ArrayList()): Parcelable