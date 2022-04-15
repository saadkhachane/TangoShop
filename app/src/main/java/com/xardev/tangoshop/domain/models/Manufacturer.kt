package com.xardev.tangoshop.domain.models

import java.io.Serializable

data class Manufacturer(
    val name: String,
    val slug: String
) : Serializable