package git.batteryeater.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import git.batteryeater.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "dvd_bounce_high_priority";
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // IMPORTANCE_HIGH permet le son, la vibration et l'affichage en bannière
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alertes DVD",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications lors des clics sur le logo");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            // Pattern : Silence, Vibration, Pause, Vibration
            channel.setVibrationPattern(new long[]{0, 250, 100, 250});

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void triggerNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Icône système par défaut
                .setContentTitle("Logo DVD")
                .setContentText("Vous avez cliqué sur le caillou !")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Pour les versions < Android 8
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true) // Disparaît quand on clique dessus
                .setDefaults(NotificationCompat.DEFAULT_ALL); // Force son + vibreur

        notificationManager.notify(1, builder.build());
    }
}