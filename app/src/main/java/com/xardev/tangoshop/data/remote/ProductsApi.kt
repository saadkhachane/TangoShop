package com.xardev.tangoshop.data.remote

import com.xardev.tangoshop.data.remote.dto.ProductDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("products")
    fun getProducts(): Single<List<ProductDTO>>

    @GET("products")
    fun getProductsByName(@Query("name") name: String): Single<List<ProductDTO>>

}