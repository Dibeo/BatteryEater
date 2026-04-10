package git.batteryeater.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.Choreographer;
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
import git.batteryeater.engine.OrientationManager;
import git.batteryeater.engine.UpdateManager;
import git.batteryeater.location.GpsManager;

public class MainActivity extends AppCompatActivity {

    private DvdEngine dvdEngine;
    private GpsManager gpsManager;

    private NotificationHelper notificationHelper;
    private OrientationManager orientationManager;
    private final Handler handler = new Handler();
    private final List<Integer> images = Arrays.asList(
            R.drawable.caillou1,
            R.drawable.caillou2,
            R.drawable.caillou3
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkForUpdates("Dibeo", "BatteryEater");

        setBrightness(0.75f);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.preferredDisplayModeId = 0;
            getWindow().setAttributes(layoutParams);
        }

        setContentView(R.layout.activity_main);

        TextView orientationTxt = findViewById(R.id.orientation_text);
        orientationManager = new OrientationManager(this, orientationTxt);

        notificationHelper = new NotificationHelper(this);
        ImageView logo = findViewById(R.id.caillou_logo);
        TextView gpsText = findViewById(R.id.gps_text);
        final View container = findViewById(R.id.container);

        dvdEngine = new DvdEngine(container, logo, images);
        gpsManager = new GpsManager(this, (lat, lon) ->
                gpsText.setText(String.format("Lat: %.6f\nLon: %.6f", lat, lon))
        );

        logo.setOnClickListener(v -> {
            notificationHelper.triggerNotification();
            dvdEngine.nextImage();
        });

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
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                dvdEngine.updateStep();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    private void requestGpsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        orientationManager.start();
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