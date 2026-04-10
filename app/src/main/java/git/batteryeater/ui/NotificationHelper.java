package git.batteryeater.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "dvd_ultra_priority";
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createUltraChannel();
    }

    private void createUltraChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alertes Critiques",
                    NotificationManager.IMPORTANCE_HIGH
            );

            long[] highIntensityPattern = {0, 500, 100, 500, 100, 1000};
            channel.setVibrationPattern(highIntensityPattern);
            channel.enableVibration(true);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM) // Utilise le volume des alarmes
                    .build();

             Uri defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI;
            channel.setSound(defaultSoundUri, audioAttributes);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void triggerNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("ALERTE CAILLOU")
                .setContentText("Contact visuel établi !")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[]{0, 500, 100, 500, 100, 1000})
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}