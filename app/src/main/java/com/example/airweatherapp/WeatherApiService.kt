package com.example.airweatherapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/"
private val paramsInterceptor = Interceptor { chain ->
    var request = chain.request()
    val newUrl = request.url.newBuilder()
        .addPathSegments("data/2.5/weather")
        .build()
    request = request.newBuilder()
        .url(newUrl)
        .build()
    chain.proceed(request)
}

private val loggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BASIC)

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(paramsInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()

private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(OPEN_WEATHER_BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface WeatherApiService {
    @GET("/")
    suspend fun getPlace(
        @Query("lat") latitude: Int,
        @Query("lon") longitude: Int
    ): Place
}

object WeatherApiImpl {
    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}