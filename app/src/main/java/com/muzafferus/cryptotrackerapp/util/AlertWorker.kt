package com.muzafferus.cryptotrackerapp.util


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.muzafferus.cryptotrackerapp.R
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.repository.AlertRepository
import com.muzafferus.cryptotrackerapp.data.repository.CryptoRepository
import com.muzafferus.cryptotrackerapp.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.math.BigDecimal


@HiltWorker
class AlertWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val alertRepository: AlertRepository,
    private val cryptoRepository: CryptoRepository
) :
    CoroutineWorker(appContext, workerParams) {

    private val CHANNEL_ID = 666

    override suspend fun doWork(): Result {
        alertRepository.getAllAlerts().collect { response ->
            if (!response.isNullOrEmpty()) {
                val cryptoGroup = arrayListOf<String>()

                for (element in response) {
                    if (!cryptoGroup.contains(element.cryptoId)) {
                        cryptoGroup.add(element.cryptoId)
                    }
                }

                for (cryptoId in cryptoGroup) {
                    val apiResponse = cryptoRepository.getPrice(cryptoId)
                    val newPrice = apiResponse.data?.get(cryptoId)?.usd ?: return@collect
                    alertRepository.getCryptoAlert(cryptoId).collect { alertList ->
                        if (!alertList.isNullOrEmpty()) {
                            for (alert in alertList) {

                                if ((alert.price.toBigDecimal() < newPrice && alert.type == 1) ||
                                    alert.price.toBigDecimal() > newPrice && alert.type == 2
                                ) {
                                    showNotification(alert, newPrice)
                                }
                            }
                        }
                    }
                }
            }
        }
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun showNotification(alert: AlertModel, newPrice: BigDecimal?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = applicationContext.getString(R.string.app_name)
        val subtitleNotification = String.format(
            "%s %s %s. %s %s",
            alert.cryptoId,
            if (alert.type == 1) "fell below" else "rose above",
            alert.price,
            "There is",
            newPrice.toString()
        )
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.setChannelId(NOTIFICATION_CHANNEL)

        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION).build()

        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

        channel.enableLights(true)
        channel.lightColor = RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(CHANNEL_ID, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }

}
