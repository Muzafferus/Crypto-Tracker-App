package com.muzafferus.cryptotrackerapp.data.repository

import com.muzafferus.cryptotrackerapp.data.remote.CryptoRemoteDataSource
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    private val remoteDataSource: CryptoRemoteDataSource
) {
    suspend fun getCrypto() = remoteDataSource.getCryptoList()

    suspend fun getPrice(cryptoId:String) = remoteDataSource.getPrice(cryptoId)

    suspend fun getDetail(cryptoId:String) = remoteDataSource.getDetail(cryptoId)
}