package com.example.wikipedialearningapp.features.search

import androidx.recyclerview.widget.DiffUtil.Callback
import com.asadkhan.wiki.base.domain.Page
import java.util.LinkedList

class WikipediaDuffUtilCallback : Callback() {

  private var oldList: List<Any?> = mutableListOf()
  private var newList: List<Any?> = mutableListOf()

  fun initialize(old: Collection<*>, new: Collection<*>): WikipediaDuffUtilCallback {
    oldList = LinkedList(old)
    newList = LinkedList(new)
    return this
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldItemPosition == newItemPosition
  }

  override fun getOldListSize(): Int {
    return oldList.size
  }

  override fun getNewListSize(): Int {
    return newList.size
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldListTemp = oldList
    val newListTemp = newList
    val any1 = oldListTemp[oldItemPosition] as Page?
    val any2 = newListTemp[newItemPosition] as Page?
    return any1?.pageid == any2?.pageid ?: false
  }
}
