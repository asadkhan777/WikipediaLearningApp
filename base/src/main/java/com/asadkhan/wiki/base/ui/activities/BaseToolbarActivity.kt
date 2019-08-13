package com.asadkhan.wiki.base.ui.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.asadkhan.wiki.base.R
import com.asadkhan.wiki.base.utils.OnQueryTextChangeListener

abstract class BaseToolbarActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fragment_holder)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.wiki_menu, menu)
    menu?.let {
      val searchItem = it.findItem(R.id.mi_action_search)
      val searchView = searchItem?.actionView as SearchView
      searchView.setOnQueryTextListener(getOnQueryTextListener())
    }
    return true
  }

  /**
   * Listener for search query update callbacks
   * */
  abstract fun getOnQueryTextListener(): OnQueryTextChangeListener

}
