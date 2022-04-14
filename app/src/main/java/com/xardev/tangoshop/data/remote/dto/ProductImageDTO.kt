package com.xardev.tangoshop.data.remote.dto

import com.xardev.tangoshop.domain.models.ProductImage

data class ProductImageDTO(
    val image: String,
    val order: Int
){
    fun toProductImage() =
        ProductImage(image, order)
}