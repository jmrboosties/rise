package fitness.classmate.net.api;

import fitness.classmate.net.model.GetSpotifyPlaylistsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyApi {

	@GET("users/{id}/playlists")
	Call<GetSpotifyPlaylistsResponse> getPlaylists(
			@Path("id") String id,
			@Query("limit") int limit
	);


}
