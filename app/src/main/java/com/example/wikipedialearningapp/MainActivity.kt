package com.example.wikipedialearningapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.asadkhan.wiki.base.tryThis
import com.asadkhan.wiki.base.ui.activities.BaseToolbarActivity
import com.asadkhan.wiki.base.utils.OnQueryTextChangeListener
import com.example.wikipedialearningapp.features.search.WikipediaSearchFragment
import timber.log.Timber

class MainActivity : BaseToolbarActivity() {

  private val wikipediaSearchFragment = WikipediaSearchFragment()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fragment_holder)
    this.addWikiSearchFragment(wikipediaSearchFragment)
  }

  private fun addWikiSearchFragment(fragment: Fragment) {
    showFragment(R.id.fragment_container, fragment)
  }

  override fun getOnQueryTextListener(): OnQueryTextChangeListener {
    return object : OnQueryTextChangeListener() {

      override fun onQueryTextChange(newText: String?): Boolean {
        Timber.w("Query changed: $newText")
        tryThis {
          wikipediaSearchFragment.SEARCH_MODE = (newText != null && newText.isNotEmpty())
          wikipediaSearchFragment.getStringProcessor().onNext(newText ?: "")
        }
        return false
      }
    }
  }
}
