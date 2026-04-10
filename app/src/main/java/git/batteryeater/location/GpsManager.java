package git.batteryeater.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

public class GpsManager {
    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private final OnLocationUpdatedListener listener;

    public interface OnLocationUpdatedListener {
        void onUpdate(double lat, double lon);
    }

    public GpsManager(Context context, OnLocationUpdatedListener listener) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.listener = listener;
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    listener.onUpdate(location.getLatitude(), location.getLongitude());
                }
            }
        };
    }

    public void startUpdates(Context context) {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
                .setMinUpdateIntervalMillis(200)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setWaitForAccurateLocation(true)
                .build();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
        }
    }

    public void stopUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}