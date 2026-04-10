package git.batteryeater.engine;

import android.content.Context;
import android.view.OrientationEventListener;
import android.widget.TextView;

public class OrientationManager {
    private OrientationEventListener orientationEventListener;
    private final Context context;
    private final TextView displayTextView;

    public OrientationManager(Context context, TextView displayTextView) {
        this.context = context;
        this.displayTextView = displayTextView;
        initListener();
    }

    private void initListener() {
        orientationEventListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == -1) return;

                String direction;
                // Détermination de l'orientation selon l'angle (0-359°)
                if (orientation >= 45 && orientation < 135) {
                    direction = "Paysage (Rotation Droite)";
                } else if (orientation >= 135 && orientation < 225) {
                    direction = "Portrait (Inversé)";
                } else if (orientation >= 225 && orientation < 315) {
                    direction = "Paysage (Rotation Gauche)";
                } else {
                    direction = "Portrait";
                }

                displayTextView.setText("Orientation : " + direction + " (" + orientation + "°)");
            }
        };
    }

    public void start() {
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    }
}