package com.example.wikipedialearningapp.features.search

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asadkhan.wiki.base.domain.Page
import com.asadkhan.wiki.base.domain.Terms
import com.asadkhan.wiki.base.domain.Thumbnail
import com.asadkhan.wiki.base.tryThis
import com.asadkhan.wiki.base.ui.widgets.BaseAdapter
import com.example.wikipedialearningapp.R

class WikipediaSearchAdapter(
  items: HashSet<Page> = linkedSetOf()
) : BaseAdapter<Page>(items = LinkedHashSet(items), itemsFull = LinkedHashSet(items)) {

  companion object {
    var SHOW_AS_LIST = true
  }

  private val diffCallback = WikipediaDuffUtilCallback()

  override fun viewHolderLayout(): Int {
    return if (SHOW_AS_LIST) {
      R.layout.wikipedia_search_result_row_list
    }
    else {
      // TODO :: Make a grid row layout if you get time
      R.layout.wikipedia_search_result_row_list
    }
  }

  override fun hasData(): Boolean {
    return itemCount > 0
  }

  override fun getViewHolder(itemView: View): RecyclerView.ViewHolder {
    return WikipediaPageViewHolder(view = itemView)
  }

  override fun update(newItems: HashSet<Any?>) {
    //val diffResult = diffResult(newItems)
    //val sizeBeforeAddition = items.size
    this.items.clear()
    this.items.addAll(newItems)
    //diffResult.dispatchUpdatesTo(this)
    notifyDataSetChanged()
    //notifyItemRangeInserted(sizeBeforeAddition, newItems.size)
  }

  @Deprecated("Deprecated in favor of update()")
  override fun append(newItems: HashSet<Any?>, refreshUI: Boolean) {
    val sizeBeforeAddition = items.size
    this.items.addAll(newItems)
    notifyItemRangeInserted(sizeBeforeAddition, newItems.size)
  }

  override fun clear(refreshUI: Boolean) {
    this.items.clear()
    notifyDataSetChanged()
  }

  override fun appendAll(newItems: Collection<Any?>, refreshUI: Boolean) {
    val sizeBeforeAddition = items.size
    this.items.addAll(newItems)
    notifyItemRangeInserted(sizeBeforeAddition, newItems.size)
    //dispatchUpdateConditionally(refreshUI, newItems)
  }

  private fun dispatchUpdateConditionally(refreshUI: Boolean, newItems: Collection<Any?>) {
//    if (refreshUI) {
//    }
//    val diffResult = diffResult(newItems)
//    diffResult.dispatchUpdatesTo(this)
    notifyDataSetChanged()
  }

  fun diffResult(newItems: Collection<Any?>): DiffUtil.DiffResult {
    val utilCallback = diffCallback.initialize(items, newItems)
    return DiffUtil.calculateDiff(utilCallback)
  }

  override fun getItemCount() = this.items.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    tryThis {
      if (holder is WikipediaPageViewHolder) {
        val elementAt = items.elementAt(position)
        val element = elementAt as Page? ?: defaultValue()
        holder.bindData(element)
      }
    }
  }

  override fun defaultValue(): Page {
    return Page(
      1,
      5043734,
      Terms(listOf("online encyclopedia that everyone can edit")),
      Thumbnail(
        50,
        46,
        "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/50px-Wikipedia-logo-v2.svg.png"
      ),
      "Wikipedia"
    )
  }
}
