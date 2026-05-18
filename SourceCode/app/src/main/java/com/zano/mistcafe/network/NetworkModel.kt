package com.zano.mistcafe.network

import com.zano.mistcafe.db.Product

data class ProductDto(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val category: String,
    val image: String,
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageUrl = image,
        category = category,
        rating = rating,
        isFavorite = false
    )
}

