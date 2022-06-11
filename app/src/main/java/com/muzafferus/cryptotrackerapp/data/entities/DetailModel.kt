package com.muzafferus.cryptotrackerapp.data.entities

data class DetailModel(
    val coingecko_rank: Int,
    val id: String,
    val image: ImageModel,
    val name: String,
    val symbol: String
)
