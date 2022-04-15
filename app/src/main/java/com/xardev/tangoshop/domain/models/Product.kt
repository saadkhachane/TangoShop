package com.xardev.tangoshop.domain.models

import java.io.Serializable

data class Product(
    val gross_price: String,
    val id: String,
    val images: List<ProductImage>,
    val manufacturer: Manufacturer,
    val product_name: String,
    val sku: String,
    val slug: String
): Serializable