package com.example.wikipedialearningapp.features.search

import android.content.Context
import android.view.View
import com.asadkhan.wiki.base.contex
import com.asadkhan.wiki.base.domain.Page
import com.asadkhan.wiki.base.getDrawableId
import com.asadkhan.wiki.base.tryThis
import com.asadkhan.wiki.base.ui.widgets.BaseViewHolder
import com.asadkhan.wiki.base.utils.ImageUtils
import com.asadkhan.wiki.base.withSubscriber
import com.example.wikipedialearningapp.R
import io.reactivex.Observable.just
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.wikipedia_search_result_row_list.view.iv_sprite
import kotlinx.android.synthetic.main.wikipedia_search_result_row_list.view.tv_sub_title
import kotlinx.android.synthetic.main.wikipedia_search_result_row_list.view.tv_title

open class WikipediaPageViewHolder(
  ctx: Context = contex(), view: View
) :
  BaseViewHolder<Page>(ctx, view) {

  var id = -1

  override fun onBind(newData: Page) {
    val tryThisResult = tryThis {
      newData.let { page ->
        println("Page :: \n$page")
        this.id = page.pageid
        bindPageDetailsAsync(newData)
        loadEntryImage(page)
      }
    }
    if (tryThisResult) {
      return
    }
    else {
      error("Data is null or invalid! Please check")
    }
  }

  private fun bindPageDetailsAsync(newData: Page?) {
    just(newData)
      .map { createDataSet(it) }
      //.asyncOp()
      .subscribe(withSubscriber(Consumer { bindFormattedDataToView(it) }))
  }

  private fun createDataSet(page: Page): HashSet<String> {
    val set = linkedSetOf(
      page.title.capitalize(),
      page.terms?.description?.reduce { acc, s -> "$acc, $s" }?.capitalize() ?: ""
    )
    val source = getImageThumbnailUrl(page)
    set.add(source)
    return set
  }

  private fun bindFormattedDataToView(set: HashSet<String>) {

    itemView.tv_title.text = set.elementAt(0)

    itemView.tv_sub_title.text = set.elementAt(1)
  }

  private fun getImageThumbnailUrl(page: Page) = page.thumbnail?.source ?: defaultUrl()

  private fun defaultUrl(): String {
    return "https://marketing.dcassetcdn.com/blog/20180612-Manual/wiki.png"
  }

  private fun loadEntryImage(it: Page, front: Boolean = true) {
    tryThis {
      val showFrontImage = if (front)
        ctx.getDrawableId("${it.pageid}.png")
      else
        ctx.getDrawableId("back/${it.pageid}.png")

      ImageUtils()
        .loadImage(
          ctx = ctx,
          view = itemView.iv_sprite,
          drawable = showFrontImage,
          url = getImageThumbnailUrl(it),
          placeHolder = R.drawable.ic_mtrl_chip_checked_circle
        )
    }
  }
}
