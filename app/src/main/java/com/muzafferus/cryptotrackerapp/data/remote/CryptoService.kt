package com.muzafferus.cryptotrackerapp.data.remote

import com.muzafferus.cryptotrackerapp.data.entities.CryptoModel
import com.muzafferus.cryptotrackerapp.data.entities.DetailModel
import com.muzafferus.cryptotrackerapp.data.entities.PriceModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoService {

    @GET("v3/coins/list")
    suspend fun getCryptoList(
        @Query("include_platform")
        isIncludePlatform: Boolean
    ): Response<List<CryptoModel>>


    @GET("v3/simple/price")
    suspend fun getPrice(
        @Query("ids")
        id: String,
        @Query("vs_currencies")
        currency: String
    ): Response<HashMap<String, PriceModel>>

    @GET("v3/coins/{id}")
    suspend fun getDetail(@Path("id") id: String): Response<DetailModel>
}