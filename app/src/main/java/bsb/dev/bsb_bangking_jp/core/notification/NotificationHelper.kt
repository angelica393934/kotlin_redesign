package bsb.dev.bsb_bangking_jp.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import bsb.dev.bsb_bangking_jp.R
import bsb.dev.bsb_bangking_jp.app.MainActivity

object NotificationHelper {

    private const val CHANNEL_ID_TRANSAKSI = "channel_transaksi"
    private const val CHANNEL_NAME_TRANSAKSI = "Notifikasi Transaksi"
    private const val CHANNEL_DESC_TRANSAKSI = "Notifikasi untuk transaksi perbankan (transfer, top up, dll)"

    private var notificationIdCounter = 1000

    /** Panggil SEKALI saat app start (mis. di BsbApplication.onCreate()). */
    fun createNotificationChannel(context: Context) {
        val soundUri = "android.resource://${context.packageName}/${R.raw.jadilahbis}".toUri()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID_TRANSAKSI,
            CHANNEL_NAME_TRANSAKSI,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = CHANNEL_DESC_TRANSAKSI
            setSound(soundUri, audioAttributes)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 100, 250)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    fun showTransaksiBerhasil(context: Context, title: String, message: String) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_TRANSAKSI)
            .setSmallIcon(R.drawable.logosplash)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE)

        try {
            NotificationManagerCompat.from(context)
                .notify(notificationIdCounter++, builder.build())
        } catch (e: SecurityException) {
            // Permission POST_NOTIFICATIONS belum diberikan user -- abaikan dengan aman.
        }
    }
}