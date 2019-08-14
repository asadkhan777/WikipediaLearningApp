package com.asadkhan.wiki.base.di;


import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.asadkhan.wiki.base.di.qualifiers.AppContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;


@Module
public class AppModule {
  
  private final Application application;
  
  
  public AppModule(Application application) {
    this.application = application;
  }
  
  
  @NonNull
  @Provides
  @Singleton
  public Application application() {
    return this.application;
  }
  
  
  @NonNull
  @Provides
  @Singleton
  @AppContext
  public Context appContext() {
    return this.application;
  }
}
