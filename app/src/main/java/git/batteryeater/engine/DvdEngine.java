package git.batteryeater.engine;

import android.view.View;
import android.widget.ImageView;
import java.util.List;

public class DvdEngine {
    private final ImageView logo;
    private final View container;
    private final List<Integer> images;

    private float speedX = 4f;
    private float speedY = 4f;
    private int currentIndex = 0;

    public DvdEngine(View container, ImageView logo, List<Integer> images) {
        this.container = container;
        this.logo = logo;
        this.images = images;
    }

    public void updateStep() {
        logo.setX(logo.getX() + speedX);
        logo.setY(logo.getY() + speedY);

        if (logo.getX() <= 0 || logo.getX() + logo.getWidth() >= container.getWidth()) {
            speedX = -speedX;
            nextImage();
        }

        if (logo.getY() <= 0 || logo.getY() + logo.getHeight() >= container.getHeight()) {
            speedY = -speedY;
            nextImage();
        }
    }

    private void nextImage() {
        if (images.isEmpty()) return;
        currentIndex = (currentIndex + 1) % images.size();
        logo.setImageResource(images.get(currentIndex));
    }
}