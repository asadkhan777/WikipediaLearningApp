package com.asadkhan.wiki.base.ui.widgets

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import io.reactivex.subjects.Subject

open class RecyclerViewScrollListener(
  private val scrollViewSubject: Subject<Boolean>,
  private val partiallyVisibleCounts: Boolean = true
) : OnScrollListener() {

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    //val firstItemVisible = isFirstItemVisible(recyclerView)
    //e("firstItemVisible $firstItemVisible")
    scrollViewSubject.onNext(partiallyVisibleCounts)
  }

  //  public fun isFirstItemVisible(
  //    recyclerView: RecyclerView,
  //    partiallyVisible: Boolean = partiallyVisibleCounts
  //  ): Boolean {
  //    val adapter = recyclerView.adapter ?: return false
  //    val itemCount = adapter.itemCount
  //    return if (itemCount != 0) {
  //      val manager = recyclerView.layoutManager
  //      val firstVisibleItemPosition = if (manager is LinearLayoutManager) {
  //        if (partiallyVisible)
  //          manager.findFirstVisibleItemPosition()
  //        else
  //          manager.findFirstCompletelyVisibleItemPosition()
  //      }
  //      else if (manager is GridLayoutManager) {
  //        if (partiallyVisible)
  //          manager.findFirstVisibleItemPosition()
  //        else
  //          manager.findFirstCompletelyVisibleItemPosition()
  //      }
  //      else {
  //        NO_POSITION
  //      }
  //      return firstVisibleItemPosition == 0
  //    }
  //    else {
  //      false
  //    }
  //  }
}
