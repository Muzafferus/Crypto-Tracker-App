package com.muzafferus.cryptotrackerapp.util

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {

    fun refreshPeriodicWork(context: Context) {
        //define constraints
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshCpnWork = PeriodicWorkRequest.Builder(
            AlertWorker::class.java,
            15, TimeUnit.MINUTES
        )
            .setInitialDelay(15, TimeUnit.MINUTES)
            .setConstraints(myConstraints)
            .addTag("myWorkManager")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "myWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork
        )

    }
}