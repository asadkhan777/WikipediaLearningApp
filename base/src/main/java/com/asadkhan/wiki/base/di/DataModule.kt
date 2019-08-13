package com.asadkhan.wiki.base.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.NonNull
import com.asadkhan.wiki.base.BuildConfig.DEBUG
import com.asadkhan.wiki.base.contex
import com.asadkhan.wiki.base.di.qualifiers.AppContext
import com.asadkhan.wiki.base.network.ApiService
import com.asadkhan.wiki.base.network.NetworkManager
import com.asadkhan.wiki.base.network.NetworkService
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers.io
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.createWithScheduler
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton
import kotlin.text.Charsets.UTF_8

@Module
class DataModule() {

  constructor(baseUrl: String) : this() {
    BASE_URL = baseUrl
  }

  @Module
  companion object {

    private const val CACHE = "Cache-Control"
    private const val SHARED_PREFERENCE_NAME = "wikipedia_app_preferences"

    @JvmStatic
    var BASE_URL = "https://en.wikipedia.org"

    val FORMAT_ONE = "yyyy-MM-dd'T'HH:mm:ss"

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun logInterceptor(): Interceptor {
      return HttpLoggingInterceptor().setLevel(BODY)
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun networkCache(app: Application): Cache {
      //setup cache
      val httpCacheDirectory = File(app.cacheDir, "responses")
      val cacheSize = 200 * 1024 * 1024L // 200 MiB
      return Cache(httpCacheDirectory, cacheSize)
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun okHttpClient(
      cache: Cache,
      loggingInterceptor: Interceptor
    ): OkHttpClient {

      return OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addNetworkInterceptor(cacheControlInterceptor())
        .cache(cache)
        .connectTimeout(120, SECONDS)
        .readTimeout(80, SECONDS)
        .writeTimeout(80, SECONDS).build()
    }

    private fun cacheControlInterceptor(): Interceptor {
        return Interceptor {
        val response = it.proceed(it.request())
        val cacheExpiryAge = 60 * 60 * 24 * 30 * 12 // One year. One year, yeah, You read that right.
        val maxAge = if (DEBUG) {
          //60 // One minute for debug version
          cacheExpiryAge
        }
        else {
          cacheExpiryAge
        }
        return@Interceptor response
          .newBuilder()
          .header(CACHE, "public, max-age=$maxAge")
          .build()
      }
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
      return Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(createWithScheduler(io())).client(okHttpClient)
        .build()
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun gson(): Gson {
      return GsonBuilder()
        .setDateFormat(FORMAT_ONE)
        .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .setPrettyPrinting()
        .create()
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun apiService(retrofit: Retrofit): ApiService {
      return retrofit.create(ApiService::class.java)
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun networkService(networkManager: NetworkManager): NetworkService {
      return networkManager
    }

    @NonNull
    @Provides
    @Singleton
    @JvmStatic
    fun sharedPreferences(@AppContext context: Context): SharedPreferences {
      return context.applicationContext.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    }

    fun loadDataFromAsset(context: Context = contex(), fileName: String = "pokedex.json"): String? {
      val json: String
      try {
        val iStream = context.assets.open(fileName)
        val size = iStream.available()
        val buffer = ByteArray(size)
        iStream.read(buffer)
        iStream.close()
        json = String(buffer, UTF_8)
      }
      catch (ex: Exception) {
        ex.printStackTrace()
        return ""
      }
      return json
    }

    //    fun convertPokedexStringToJson(jsonString: String): List<PokemonData> {
    //      return tryHere({
    //        start()
    //        val wrapper = tryHere({
    //          gson().fromJson(jsonString, PokedexListWrapper::class.java)
    //        }, PokedexListWrapper(LinkedList()))
    //        stop("convertPokedexStringToJson")
    //        return@tryHere wrapper.pokemon
    //      }, mutableListOf())
    //    }
    //
    //    private fun onlyEmptyList(it: List<Int?>) = it.isEmpty() || it[0] == null
    //
    //    private fun <T> dbObserver(): Observer<T> {
    //      return withSubscriber(Consumer { e("Yay! DB Populated!") })
    //    }

  }

}
