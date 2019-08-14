package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Redirect(
  @SerializedName("from")
  val from: String, // Sachin The Film
  @SerializedName("index")
  val index: Int, // 4
  @SerializedName("to")
  val to: String // Sachin: A Billion Dreams
) : Parcelable
