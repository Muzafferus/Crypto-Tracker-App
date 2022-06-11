package com.muzafferus.cryptotrackerapp.data.repository

import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.local.AlertDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlertRepository @Inject constructor(
    private val localDataSource: AlertDao
) {
    fun getAllAlerts() = localDataSource.getAllAlerts()

    fun getCryptoAlert(cryptoId: String) = localDataSource.getCryptoAlerts(cryptoId)

    suspend fun insert(alert: AlertModel) {
        localDataSource.insert(alert)
    }

    suspend fun delete(alert: AlertModel) {
        localDataSource.delete(alert)
    }



}