package me.ajiew.jithub.data.service

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


object NetworkCreator {

    private const val BASE_URL = "https://api.github.com/"

    private const val BASE_TRENDING_URL = "https://gtrend.yapie.me/"


    private const val DEFAULT_TIMEOUT: Long = 60

    private const val KEEP_ALIVE_DURATION: Long = 60

    private const val MAX_IDLE_CONNECTION = 8

    private const val HTTP_CACHE_SIZE: Long = 24 * 1024 * 1024

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor { Timber.tag("OkHttp").v(it) }
            .setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(MAX_IDLE_CONNECTION, KEEP_ALIVE_DURATION, TimeUnit.SECONDS))
        .build()

    private val baseTrendingRetrofit = Retrofit.Builder()
        .baseUrl(BASE_TRENDING_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <T> createBaseTrending(serviceClass: Class<T>): T = baseTrendingRetrofit.create(serviceClass)
}