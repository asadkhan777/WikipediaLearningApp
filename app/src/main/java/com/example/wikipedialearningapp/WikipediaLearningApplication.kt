package com.example.wikipedialearningapp

import com.asadkhan.wiki.base.AbstractApplication
import com.asadkhan.wiki.base.di.AppComponent
import com.asadkhan.wiki.base.di.AppModule
import com.asadkhan.wiki.base.di.DaggerAppComponent

class WikipediaLearningApplication : AbstractApplication() {

  override fun createComponent(): AppComponent {
    return DaggerAppComponent.builder().appModule(AppModule(this)).build()
  }

}
