package com.asadkhan.wiki.base.utils

import androidx.appcompat.widget.SearchView.OnQueryTextListener
import timber.log.Timber.w

abstract class OnQueryTextChangeListener : OnQueryTextListener {

  override fun onQueryTextSubmit(query: String?): Boolean {
    w("Submit query: $query")
    return false
  }
}
