package com.xardev.tangoshop.data.repositories

import com.xardev.tangoshop.data.remote.ProductsApi
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.domain.repositories.ProductRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : ProductRepository {

    override fun getProducts(): Single<List<Product>> {
        return api.getProducts().map { it.map { pr -> pr.toProduct() } }
    }

    override fun getProductsByName(name: String): Single<List<Product>> {
        return api.getProductsByName(name).map { it.map { pr -> pr.toProduct() } }
    }

}