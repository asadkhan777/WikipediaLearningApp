package com.asadkhan.wiki.base.ui.widgets

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.asadkhan.wiki.base.AbstractApplication

abstract class BaseViewHolder<DATA>(

  val ctx: Context = AbstractApplication.component().applicationContext,
  val v: View,
  var data: DATA? = null

) : RecyclerView.ViewHolder(v) {

  // No initialization in constructor
  // Data will be bound in the onBind() callback

  fun bindData(newData: DATA) {
    if (newData == null) {
      error("Data in ViewHolder cannot be null! ViewHolder: ${javaClass.simpleName}")
    }
    this.data = newData
    onBind(newData as DATA)
  }

  abstract fun onBind(newData: DATA)

}
