package bsb.dev.bsb_bangking_jp.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import bsb.dev.bsb_bangking_jp.MainActivity
import bsb.dev.bsb_bangking_jp.R

/**
 * Helper notifikasi lokal (bukan push/FCM) dengan suara custom -- padanan
 * "notifikasi transaksi berhasil" yang muncul di aplikasi bank sungguhan
 * setelah transfer sukses.
 */
object NotificationHelper {

    private const val CHANNEL_ID_TRANSAKSI = "channel_transaksi"
    private const val CHANNEL_NAME_TRANSAKSI = "Notifikasi Transaksi"
    private const val CHANNEL_DESC_TRANSAKSI = "Notifikasi untuk transaksi perbankan (transfer, top up, dll)"

    private var notificationIdCounter = 1000

    /** Panggil SEKALI saat app start (mis. di BsbApplication.onCreate()). */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/${R.raw.jadilahbisss}"
        )

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID_TRANSAKSI,
            CHANNEL_NAME_TRANSAKSI,
            NotificationManager.IMPORTANCE_HIGH, // 🔹 HIGH supaya heads-up + suara benar-benar bunyi
        ).apply {
            description = CHANNEL_DESC_TRANSAKSI
            setSound(soundUri, audioAttributes)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 100, 250)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    /**
     * Tampilkan notifikasi transaksi berhasil dengan suara custom.
     * @param title judul notifikasi, mis. "Transfer Berhasil"
     * @param message isi notifikasi, mis. "Transfer Rp500.000 ke Budi Santoso berhasil"
     */
    fun showTransaksiBerhasil(context: Context, title: String, message: String) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/${R.raw.jadilahbisss}"
        )

        // Tap notifikasi -> buka MainActivity (arahkan ke Beranda).
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
            .setSmallIcon(R.drawable.logosplash) // 🔹 ganti dengan ikon monokrom khusus notif kalau ada
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE)

        // Android 7.1 ke bawah -- channel tidak berlaku, suara harus di-set manual di builder.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(soundUri)
        }

        try {
            NotificationManagerCompat.from(context)
                .notify(notificationIdCounter++, builder.build())
        } catch (e: SecurityException) {
            // Permission POST_NOTIFICATIONS belum diberikan user -- abaikan dengan aman.
        }
    }
}