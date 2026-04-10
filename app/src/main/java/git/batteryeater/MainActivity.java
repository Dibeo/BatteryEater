package git.batteryeater;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

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
        imageView = findViewById(R.id.dvd_logo);

        if (!listeImages.isEmpty()) {
            imageView.setImageResource(listeImages.get(0));
        }

        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
}