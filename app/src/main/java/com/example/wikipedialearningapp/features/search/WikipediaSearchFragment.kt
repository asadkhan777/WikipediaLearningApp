package com.example.wikipedialearningapp.features.search

import android.net.Uri.parse
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.asadkhan.wiki.base.*
import com.asadkhan.wiki.base.domain.Page
import com.asadkhan.wiki.base.ui.fragments.BaseFragment
import com.asadkhan.wiki.base.ui.widgets.RecyclerViewScrollLastViewListener
import com.asadkhan.wiki.base.ui.widgets.RecyclerViewScrollListener
import com.example.wikipedialearningapp.R
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.subjects.PublishSubject.create
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_wikipedia_search.*
import timber.log.Timber.e
import java.util.concurrent.TimeUnit.MILLISECONDS

class WikipediaSearchFragment : BaseFragment() {

  var SEARCH_MODE = false
  var IS_LOADING = false

  val builder = CustomTabsIntent.Builder().addDefaultShareMenuItem().enableUrlBarHiding()

  private var latestSearchTerm = ""
  private var searchAdapter: WikipediaSearchAdapter? = null

  private var scrollSubject = create<Boolean>()
  private var fetchNextSubject = create<Boolean>()
  private var searchStringSubject = create<String>()

  var scrollListener = RecyclerViewScrollLastViewListener(fetchNextSubject)

  override fun getLayoutId() = R.layout.fragment_wikipedia_search

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tryThis { viewStateInitialization() }
  }

  private fun viewStateInitialization() {

    showDataView() // Initial loading screen
    showEmptyState()

    bindSwipeRefreshAction()

    bindCardClickAction()

    initializeAdapter()

    bindScrollUpFabAction()

    subscribeToLazyLoadOnListScrollEvents()
    //subscribeToListScrollEvents()

    subscribeToSearchStrings()
  }

  private fun bindSwipeRefreshAction() {
    srl_swipe_refresh.setOnRefreshListener {
      showLoading()
      e("Pull-To-Refresh")
      // Why clear data if HashSet will handle duplicate management?
      viewModel().clearData()
      val subscribe = viewModel()
        .searchWikipedia(latestSearchTerm) // On swipe refresh event
        .doOnNext { e("List pulled from Data Source: ${it.size}") }
        .asyncOp()
        .doOnNext {
          adapter().clear(false)
        }
        .subscribe(this::updateAdapter) {
          // Pull to refresh
          hideLoading()
          it.printStackTrace()
          bindSwipeRefreshAction()
        }
      bin.add(subscribe)
    }
  }

  private fun bindCardClickAction() {
    val subscribe = adapter().clickStream()
      .subscribe(::launchDetailsPage, Throwable::printStackTrace)
    bin.add(subscribe)
  }

  private fun initializeAdapter(): Boolean {
    return tryThis {
      rv_pokemon_list.addOnScrollListener(RecyclerViewScrollListener(scrollSubject))
      rv_pokemon_list.addOnScrollListener(scrollListener)
      rv_pokemon_list.layoutManager = LinearLayoutManager(context)
      //rv_pokemon_list.layoutManager = LinearLayoutManager(context)
      rv_pokemon_list.adapter = adapter()
      true
    }
  }

  private fun bindScrollUpFabAction() {
    //    fab_scroll_up.setOnClickListener {
    //      tryThis {
    //        rv_pokemon_list.scrollToPosition(0)
    //      }
    //    }
  }

  private fun launchDetailsPage(it: Page?) {
    if (it == null) {
      val err = "Wikipedia article data should not be null"
      e(err)
      showErrorView(err)
      return
    }
    val url = getPageUrlById("${it.pageid}")
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, parse(url))
  }

  private fun getPageUrl(it: Page) = "https://en.wikipedia.org/?curid=${it.pageid}"
  private fun getPageUrlById(it: String) = "https://en.wikipedia.org/?curid=$it"

  private fun subscribeToLazyLoadOnListScrollEvents() {
    tryThis {
      // End of list reached event
      val subscribe = fetchNextSubject
        //.debounce(300, MILLISECONDS, io())
        .doOnNext {
          println("End of line reached!")
          println("IS_LOADING: $IS_LOADING")
        }
        //.filter { !IS_LOADING } // List Scroll Loading stream event
        .switchMap { viewModel().searchNext() }
        .asyncOp()
        .subscribe(this::updateAdapter) {
          tryThis {
            if (adapter().hasData()) {
              showDataView()
              showDataState()
              toast("Something went wrong")
            }
            else {
              showErrorView()
            }
            it?.printStackTrace()
            fetchNextSubject = create()
            scrollListener = RecyclerViewScrollLastViewListener(fetchNextSubject)
            rv_pokemon_list.addOnScrollListener(scrollListener)
            subscribeToLazyLoadOnListScrollEvents()
          }
        }
      bin.addAll(subscribe)
    }
  }

  private fun adapter(): WikipediaSearchAdapter {
    if (searchAdapter != null) {
      return searchAdapter as WikipediaSearchAdapter
    }
    if (rv_pokemon_list.adapter != null) {
      searchAdapter = rv_pokemon_list.adapter as WikipediaSearchAdapter
      return searchAdapter as WikipediaSearchAdapter
    }
    if (searchAdapter == null) {
      searchAdapter = WikipediaSearchAdapter()
    }
    if (searchAdapter != null) {
      return searchAdapter as WikipediaSearchAdapter
    }
    error("Adapter retrieval failed. All sources null. Please check owner instantiation")
  }

  private fun subscribeToSearchStrings() {
    val subscribe = getStringProcessor()
      // Empty event is triggered every time search is opened, so skip it
      .skip(1)
      // Skip unchanged queries
      .distinctUntilChanged()
      // Take the latest value every X ms
      .debounce(500, MILLISECONDS, io())
      .uiThread()
      .subscribe(this::submitQueryString, {
        tryThis {
          it.printStackTrace()
          subscribeToSearchStrings()
        }
      }, {
        IS_LOADING = false
      })
    bin.add(subscribe)
  }

  private fun submitQueryString(query: String) {
    tryThis {
      IS_LOADING = true
      srl_swipe_refresh.isRefreshing = true
      if (!adapter().hasData()) {
        showLoading()
      }
      if (query.isEmpty()) {
        viewModel().clearData()
        adapter().clear(true)
        showDataView()
        showEmptyState()
      }
      latestSearchTerm = query
      viewModel()
        .searchWikipedia(query)
        .asyncOp()
        .subscribe(this::updateAdapter) {
          it.printStackTrace()
          showErrorView()
        }
    }
    //tryThis { observePokemonLiveData(query, true) }
    e("Searched for this word: $query")
  }

  private fun updateAdapter(it: List<Page>?) {
    println("Received Data: $it")
    tryThis {
      showDataView()
      if (it.isNullOrEmpty()) {
        if (adapter().hasData()) {
          toast("End of the search results has been reached.")
          showDataState()
        }
        else {
          showEmptyState()
        }
      }
      else {
        showDataState()

        if (adapter().hasData()) {
          adapter().appendAll(LinkedHashSet(it), true)
        }
        else {
          adapter().update(LinkedHashSet(it))
        }
      }
      srl_swipe_refresh.isRefreshing = false
      true
    }
  }

  private fun showEmptyState() {
    rv_pokemon_list.hide()
    iv_list_loading.show()
    tv_list_loading.show()
  }

  private fun showDataState() {
    rv_pokemon_list.show()
    iv_list_loading.hide()
    tv_list_loading.hide()
  }

  fun getStringProcessor(): Subject<String> {
    if (searchStringSubject.hasComplete() || searchStringSubject.hasThrowable()) {
      searchStringSubject = create()
    }
    return searchStringSubject
  }

  private fun viewModel(): WikipediaSearchViewModel {
    val viewModel = ViewModelProviders.of(this).get(WikipediaSearchViewModel::class.java)
    viewModel.contex = activity
    return viewModel
  }

  override fun showDataView() {
    super.showDataView()
    IS_LOADING = false
    loading.hide()
    data.show()
    error.hide()
  }

  override fun showErrorView(errorMessage: String) {
    super.showErrorView(errorMessage)
    IS_LOADING = false
    loading.hide()
    data.hide()
    error.show()
  }

  override fun showLoading() {
    super.showLoading()
    IS_LOADING = true
    loading.show()
    data.hide()
    error.hide()
  }

  override fun hideLoading() {
    super.hideLoading()
    IS_LOADING = false
    loading.hide()
  }

}
