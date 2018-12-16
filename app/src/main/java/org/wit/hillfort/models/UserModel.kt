package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     var email: String = "",
                     var passwordHash: String = "",
                     @Embedded var hillforts: MutableList<HillfortModel> = ArrayList()): Parcelable