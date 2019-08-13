package com.asadkhan.wiki.base.di;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.asadkhan.wiki.base.di.qualifiers.AppContext;
import com.asadkhan.wiki.base.network.NetworkService;
import com.google.gson.Gson;
import dagger.Component;

import javax.inject.Singleton;


@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                //ServicesModule.class
        }
)
public interface AppComponent {

    @AppContext
    @NonNull
    Context getApplicationContext();


    @NonNull
    Application getApplication();


    @NonNull
    NetworkService networkService();


    @NonNull
    Gson gson();


    SharedPreferences sharedPreferences();

}
