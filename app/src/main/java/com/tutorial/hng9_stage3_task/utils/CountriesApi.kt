package com.tutorial.hng9_stage3_task.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tutorial.hng9_stage3_task.models.main.Countries
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountriesApi{
    @GET("all")
    suspend fun getAllCountries(): Response<Countries>
}


private const val BASE_URL = "https://restcountries.com/v3.1/"

private val moshi =  Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .client(getOkHttp())
    .addConverterFactory(GsonConverterFactory.create())//MoshiConverterFactory.create(moshi)
    .baseUrl(BASE_URL)
    .build()

object ApiService{
    val retrofitApiService: CountriesApi by lazy {
        retrofit.create(CountriesApi::class.java)
    }
}

fun getOkHttp(): OkHttpClient {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder().addInterceptor(logger).build()
}