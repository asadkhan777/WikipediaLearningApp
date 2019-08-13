package com.asadkhan.wiki.base

import android.app.Application
import com.asadkhan.wiki.base.di.AppComponent
import dagger.internal.Preconditions
import timber.log.Timber.DebugTree
import timber.log.Timber.plant

/**
 * The base application class responsible for exposing Dagger injection
 * */
abstract class AbstractApplication : Application() {
  companion object {

    var component: AppComponent? = null

    fun component(): AppComponent {
      val errorMessage = "Component cannot be null! Please check if you are instantiating DaggerAppComponent Class"
      return Preconditions.checkNotNull(component!!, errorMessage)
    }
  }

  override fun onCreate() {
    super.onCreate()
    component = createComponent()
    plantTimber()
  }

  private fun plantTimber() {
    if (BuildConfig.DEBUG) {
      plant(DebugTree())
    }
    else {
      // Plant Crashlytics tree for prod
    }
  }

  abstract fun createComponent(): AppComponent
}
