package com.xardev.tangoshop.data.remote.dto

import com.xardev.tangoshop.domain.models.Manufacturer

data class ManufacturerDTO(
    val name: String,
    val slug: String
){
    fun toManufacturer() = Manufacturer(name, slug)
}