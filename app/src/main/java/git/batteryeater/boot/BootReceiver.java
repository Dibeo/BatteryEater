package git.batteryeater.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import git.batteryeater.service.AnnoyingService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AnnoyingService.class);
        context.startService(i);
    }
}