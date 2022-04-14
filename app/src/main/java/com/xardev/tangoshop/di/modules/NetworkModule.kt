package com.xardev.tangoshop.di.modules

import com.xardev.tangoshop.core.Constants
import com.xardev.tangoshop.data.remote.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun getRetrofit() : Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = Level.BASIC

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.API_BASE_URL)
            .build()
    }

    @Provides
    fun getProductsApi(retrofit: Retrofit) : ProductsApi{
        return retrofit.create(ProductsApi::class.java)
    }

}