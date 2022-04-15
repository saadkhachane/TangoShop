package com.xardev.tangoshop.di.modules

import com.xardev.tangoshop.core.Constants
import com.xardev.tangoshop.data.remote.ProductsApi
import com.xardev.tangoshop.data.repositories.ProductRepositoryImpl
import com.xardev.tangoshop.domain.repositories.ProductRepository
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
class RepositoryModule {

    @Provides
    fun getProductRepository(api: ProductsApi) : ProductRepository{
        return ProductRepositoryImpl(api)
    }

}