package com.xardev.tangoshop.data.remote.dto

import com.xardev.tangoshop.domain.models.Product

data class ProductDTO(
    val gross_price: String,
    val id: String,
    val images: List<ProductImageDTO>,
    val manufacturer: ManufacturerDTO,
    val product_name: String,
    val sku: String,
    val slug: String
) {

    fun toProduct() =
        Product(
            gross_price,
            id,
            images.map { it.toProductImage() },
            manufacturer.toManufacturer(),
            product_name,
            sku,
            slug
        )
}