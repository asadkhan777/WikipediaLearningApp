package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Thumbnail(
  @SerializedName("height")
  val height: Int, // 50
  @SerializedName("width")
  val width: Int, // 36
  @SerializedName("source")
  val source: String // https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Sachin_Bansal.jpg/36px-Sachin_Bansal.jpg

) : Parcelable
