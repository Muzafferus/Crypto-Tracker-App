package com.muzafferus.cryptotrackerapp.data.remote

import javax.inject.Inject

class CryptoRemoteDataSource @Inject constructor(
    private val cryptoService: CryptoService
) : BaseDataSource() {

    suspend fun getCryptoList() = getResult { cryptoService.getCryptoList(false) }

    suspend fun getPrice(cryptoId: String) = getResult { cryptoService.getPrice(cryptoId, "usd") }

    suspend fun getDetail(cryptoId: String) = getResult { cryptoService.getDetail(cryptoId) }
}