package git.batteryeater.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import git.batteryeater.BuildConfig;
import git.batteryeater.misc.GithubRelease;
import git.batteryeater.misc.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateManager {
    private static final String BASE_URL = "https://api.github.com/";
    private final Context context;

    public UpdateManager(Context context) {
        this.context = context;
    }

    public void checkForUpdates(String owner, String repo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubService service = retrofit.create(GithubService.class);
        service.getLatestRelease(owner, repo).enqueue(new Callback<GithubRelease>() {
            @Override
            public void onResponse(Call<GithubRelease> call, Response<GithubRelease> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String latestVersion = response.body().tagName;
                    String currentVersion = "v" + BuildConfig.VERSION_NAME;

                    if (!latestVersion.equals(currentVersion)) {
                        showUpdateDialog(latestVersion, owner, repo);
                    }
                }
            }

            @Override
            public void onFailure(Call<GithubRelease> call, Throwable t) {
                // Échec silencieux (pas d'internet, etc.)
            }
        });
    }

    private void showUpdateDialog(String newVersion, String owner, String repo) {
        new AlertDialog.Builder(context)
                .setTitle("Mise à jour disponible")
                .setMessage("Une nouvelle version (" + newVersion + ") est disponible sur GitHub.")
                .setPositiveButton("Télécharger", (dialog, which) -> {
                    String url = "https://github.com/" + owner + "/" + repo + "/releases/latest";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                })
                .setNegativeButton("Plus tard", null)
                .show();
    }
}