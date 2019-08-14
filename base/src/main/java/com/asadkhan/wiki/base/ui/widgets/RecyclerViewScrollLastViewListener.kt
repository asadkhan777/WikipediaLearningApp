package com.asadkhan.wiki.base.ui.widgets

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import io.reactivex.subjects.Subject

open class RecyclerViewScrollLastViewListener(private var fetchNextSubject: Subject<Boolean>) : OnScrollListener() {

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    if (isLastItemDisplaying(recyclerView)) {
      endOfList()
    }
  }

  private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
    val result = isLastItemDisplaying(recyclerView, 1)
    println("LastVisibleItemPosition Result: $result")
    return result
  }

  private fun endOfList() {
    fetchNextSubject.onNext(true)
  }

  private fun isLastItemDisplaying(recyclerView: RecyclerView, threshold: Int): Boolean {
    val adapter = recyclerView.adapter ?: return false
    val itemCount = adapter.itemCount
    return if (itemCount != 0) {
      var lastVisibleItemPosition = NO_POSITION
      val manager = recyclerView.layoutManager
      if (manager is LinearLayoutManager) {
        lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition()
      }
      else if (manager is GridLayoutManager) {
        lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition()
      }
      val result = lastVisibleItemPosition != NO_POSITION && lastVisibleItemPosition == adapter.itemCount - threshold
      result
    }
    else {
      false
    }
  }
}
