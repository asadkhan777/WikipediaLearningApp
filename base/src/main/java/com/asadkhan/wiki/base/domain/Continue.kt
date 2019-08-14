package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Continue(
  @SerializedName("continue")
  val continueX: String, // gpsoffset||
  @SerializedName("gpsoffset")
  val gpsoffset: Int // 10
) : Parcelable
