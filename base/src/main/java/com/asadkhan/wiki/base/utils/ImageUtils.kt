package com.asadkhan.wiki.base.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.asadkhan.wiki.base.R
import com.asadkhan.wiki.base.isNetworkAvailable
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL

class ImageUtils {

  @JvmOverloads
  fun loadImage(
    ctx: Context,
    view: ImageView,
    @IdRes @DrawableRes drawable: Int,
    url: String,
    placeHolder: Int = R.drawable.ic_loading,
    fallback: Int = R.drawable.ic_loading
  ) {

    GlideApp
      .with(ctx)
      .load(url)
      .optionalCenterCrop()
      .fallback(fallback)
      .error(fallback)
      .placeholder(placeHolder)
      .diskCacheStrategy(ALL)
      .onlyRetrieveFromCache(!ctx.isNetworkAvailable())
      .into(view)
  }

}
