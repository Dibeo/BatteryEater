package git.batteryeater.misc;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {
    @GET("repos/{owner}/{repo}/releases/latest")
    Call<GithubRelease> getLatestRelease(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
