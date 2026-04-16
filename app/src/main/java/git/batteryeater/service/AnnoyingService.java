package git.batteryeater.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class AnnoyingService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (v != null) {
            long[] pattern = {0, 100, 500, 100, 500, 100};
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                v.vibrate(pattern, -1);
            }
        }

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}