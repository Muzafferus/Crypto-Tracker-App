package com.muzafferus.cryptotrackerapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "alerts")
data class AlertModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val type: Int, //max=2, min=1
    val cryptoId: String,
    val price: String
)
