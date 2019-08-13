package com.asadkhan.wiki.base.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Query(
  @SerializedName("pages")
  val pages: List<Page>,
  @SerializedName("redirects")
  val redirects: List<Redirect>
) : Parcelable
