package com.asadkhan.wiki.base.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WikipediaSearchResults(
  @SerializedName("batchcomplete")
  val batchcomplete: Boolean, // true
  @SerializedName("continue")
  val continueX: Continue,
  @SerializedName("query")
  val query: Query
) : Parcelable
