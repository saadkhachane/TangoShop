package com.xardev.tangoshop.domain.repositories

import com.xardev.tangoshop.domain.models.Product
import io.reactivex.rxjava3.core.Single

interface ProductRepository {

    fun getProducts() : Single<List<Product>>
    fun getProductsByName(name: String) : Single<List<Product>>
}