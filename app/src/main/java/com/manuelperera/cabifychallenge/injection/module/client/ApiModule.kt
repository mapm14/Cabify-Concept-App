package com.manuelperera.cabifychallenge.injection.module.client

import com.manuelperera.cabifychallenge.BuildConfig
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClientImpl
import com.manuelperera.cabifychallenge.client.retrofit.RetrofitEstimateApiClient
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .addNetworkInterceptor({ chain ->
                    val request = chain.request().newBuilder().addHeader("Content-Type", "application/json").build()
                    chain.proceed(request)
                })
                .addNetworkInterceptor({ chain ->
                    val request = chain.request().newBuilder().addHeader("Authorization", "Bearer 6o_FrppOEQ5RrCkBOEKaBM-puJleMKrRn5nW_cy7H9Y").build()
                    chain.proceed(request)
                })
                .addNetworkInterceptor({ chain ->
                    val request = chain.request().newBuilder().addHeader("Accept-Language", "es-ES").build()
                    chain.proceed(request)
                })
                .build()
    }

    @Provides
    @Singleton
    fun estimateApiClient(client: OkHttpClient): EstimateApiClient {
        return EstimateApiClientImpl(Retrofit.Builder()
                .baseUrl("https://test.cabify.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(RetrofitEstimateApiClient::class.java))
    }

}