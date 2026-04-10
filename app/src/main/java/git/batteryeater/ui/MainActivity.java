package git.batteryeater.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.List;

import git.batteryeater.R;
import git.batteryeater.engine.DvdEngine;
import git.batteryeater.location.GpsManager;

public class MainActivity extends AppCompatActivity {

    private DvdEngine dvdEngine;
    private GpsManager gpsManager;
    private final Handler handler = new Handler();
    private final List<Integer> images = Arrays.asList(
            R.drawable.caillou1,
            R.drawable.caillou2,
            R.drawable.caillou3
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBrightness(0.75f);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.caillou_logo);
        TextView gpsText = findViewById(R.id.gps_text);
        final View container = findViewById(R.id.container);

        dvdEngine = new DvdEngine(container, logo, images);
        gpsManager = new GpsManager(this, (lat, lon) ->
                gpsText.setText(String.format("Lat: %.6f\nLon: %.6f", lat, lon))
        );

        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                requestGpsPermission();
                startLoop();
            }
        });
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dvdEngine.updateStep();
                handler.postDelayed(this, 16);
            }
        }, 16);
    }

    private void requestGpsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsManager.startUpdates(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsManager.stopUpdates();
    }

    private void setBrightness(float brightnessValue) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightnessValue;
        getWindow().setAttributes(layoutParams);
    }
}