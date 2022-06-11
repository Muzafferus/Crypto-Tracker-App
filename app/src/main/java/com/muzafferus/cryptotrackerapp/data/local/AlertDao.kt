package com.muzafferus.cryptotrackerapp.data.local

import androidx.room.*
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts")
    fun getAllAlerts(): Flow<List<AlertModel>>

    @Query("SELECT * FROM alerts WHERE cryptoId = :cryptoId")
    fun getCryptoAlerts(cryptoId: String): Flow<List<AlertModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertModel)

    @Delete
    suspend fun delete(alert: AlertModel)

}