package git.batteryeater;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private View container;

    private float speedX = 4f;
    private float speedY = 4f;

    private int currentImageIndex = 0;

    private final Handler handler = new Handler();
    private final List<Integer> listeImages = new ArrayList<>();

    private TextView gpsText;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private void initialiserListe() {
        listeImages.add(R.drawable.caillou1);
        listeImages.add(R.drawable.caillou2);
        listeImages.add(R.drawable.caillou3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiserListe();

        container = findViewById(R.id.container);
        imageView = findViewById(R.id.caillou_logo);

        if (!listeImages.isEmpty()) {
            imageView.setImageResource(listeImages.get(0));
        }

        gpsText = findViewById(R.id.gps_text);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configuration de la mise à jour GPS
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    gpsText.setText("Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude());
                }
            }
        };

        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startLocationUpdates();
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveLogo();
                handler.postDelayed(this, 16);
            }
        }, 16);
    }

    private void moveLogo() {
        imageView.setX(imageView.getX() + speedX);
        imageView.setY(imageView.getY() + speedY);

        if (imageView.getX() <= 0 || imageView.getX() + imageView.getWidth() >= container.getWidth()) {
            speedX = -speedX;
            changeImage();
        }

        if (imageView.getY() <= 0 || imageView.getY() + imageView.getHeight() >= container.getHeight()) {
            speedY = -speedY;
            changeImage();
        }
    }

    private void changeImage() {
        if (listeImages.isEmpty()) return;
        currentImageIndex = (currentImageIndex + 1) % listeImages.size();
        imageView.setImageResource(listeImages.get(currentImageIndex));
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}