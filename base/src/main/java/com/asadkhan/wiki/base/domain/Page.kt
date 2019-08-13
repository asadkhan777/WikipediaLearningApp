package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Page(
  @SerializedName("index")
  val index: Int, // 2
//  @SerializedName("ns")
//  val ns: Int, // 0
  @SerializedName("pageid")
  val pageid: Int, // 61165764
  @SerializedName("terms")
  val terms: Terms?,
  @SerializedName("thumbnail")
  val thumbnail: Thumbnail?,
  @SerializedName("title")
  val title: String // Sachin Tanwar
) : Parcelable
