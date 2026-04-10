package git.batteryeater.misc;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Modèle pour la réponse GitHub
public class GithubRelease {
    @SerializedName("tag_name")
    public String tagName;
}

