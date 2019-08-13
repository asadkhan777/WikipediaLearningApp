package com.example.wikipedialearningapp.features.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.asadkhan.wiki.base.domain.Page
import com.asadkhan.wiki.base.domain.WikipediaSearchResults
import com.asadkhan.wiki.base.networkService
import io.reactivex.Observable
import io.reactivex.Observable.just
import retrofit2.Response

class WikipediaSearchViewModel : ViewModel() {

  var contex: Context? = null
  private var currentOffset = 0
  var currentQuery = ""

  private val defaults = defaultMap()

  fun searchWikipedia(text: String = "", resetOffset: Boolean = true): Observable<List<Page>> {
    currentQuery = text
    if (resetOffset) {
      currentOffset = 0
    }
    return searchNext()
  }

  fun searchNext(): Observable<List<Page>> {
    println("Current Query: $currentQuery")
    println("Current Offset: $currentOffset")

    if (currentQuery.isEmpty()) {
      return just(listOf())
    }
    val queryParams = getDefaultQuery(currentQuery)
    return networkService()
      .getWithQueries(WikipediaSearchResults::class.java, "/w/api.php", queryParams)
      .switchMap { getResponseResult(it) }
      .map { it.body()?.query?.pages ?: listOf() }
      //.doOnNext { println("Sending back list of data: $it") }
  }

  private fun getResponseResult(it: Response<WikipediaSearchResults>): Observable<Response<WikipediaSearchResults>> {
    if (it.isSuccessful) {
      currentOffset += 10
      return just(it)
    }
    currentQuery = ""
    error("API Call Failed.")
  }

  private fun getDefaultQuery(text: String): Map<String, String> {
    return defaults.plus("gpssearch" to text).plus( "gpsoffset" to "$currentOffset")
  }

  private fun defaultMap(): LinkedHashMap<String, String> {
    val map = linkedMapOf<String, String>()
    map["action"] = "query"
    map["format"] = "json"
    map["prop"] = "pageimages|pageterms"
    map["generator"] = "prefixsearch"
    map["redirects"] = "1"
    map["formatversion"] = "2"
    map["piprop"] = "thumbnail"
    map["pithumbsize"] = "70"
    map["pilimit"] = "10"
    map["wbptterms"] = "description"
    map["gpssearch"] = "wikipedia"
    map["gpslimit"] = "10"
    map["gpsoffset"] = "$currentOffset"
    return map
  }

  fun clearData() {

  }
}
