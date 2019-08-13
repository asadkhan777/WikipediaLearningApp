package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Terms(
  @SerializedName("description")
  val description: List<String>
) : Parcelable
