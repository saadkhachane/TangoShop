package com.xardev.tangoshop.domain.models

import java.io.Serializable

data class ProductImage(
    val image: String,
    val order: Int
) : Serializable