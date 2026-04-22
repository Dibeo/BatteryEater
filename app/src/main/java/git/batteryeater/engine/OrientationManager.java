package git.batteryeater.engine;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.view.OrientationEventListener;
import android.widget.TextView;

public class OrientationManager {
    private OrientationEventListener orientationEventListener;
    private final Context context;
    private final TextView displayTextView;
    private final CameraManager cameraManager;
    private String cameraId;

    public OrientationManager(Context context, TextView displayTextView) {
        this.context = context;
        this.displayTextView = displayTextView;
        this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            this.cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        initListener();
    }

    private void initListener() {
        orientationEventListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == -1) return;

                String direction;
                boolean isLandscape;

                if (orientation >= 45 && orientation < 135) {
                    direction = "Paysage (Rotation Droite)";
                    isLandscape = true;
                } else if (orientation >= 135 && orientation < 225) {
                    direction = "Portrait (Inversé)";
                    isLandscape = false;
                } else if (orientation >= 225 && orientation < 315) {
                    direction = "Paysage (Rotation Gauche)";
                    isLandscape = true;
                } else {
                    direction = "Portrait";
                    isLandscape = false;
                }

                displayTextView.setText("Orientation : " + direction + " (" + orientation + "°)");
                toggleFlash(isLandscape);
            }
        };
    }

    private void toggleFlash(boolean enable) {
        if (cameraId == null) return;
        try {
            cameraManager.setTorchMode(cameraId, enable);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    }

    public void stop() {
        orientationEventListener.disable();
        toggleFlash(false);
    }
}