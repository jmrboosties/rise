package fitness.classmate.net.api;

import fitness.classmate.net.model.GetSpotifyPlaylistTracksResponse;
import fitness.classmate.net.model.GetSpotifyPlaylistsResponse;
import fitness.classmate.net.model.SpotifyMe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface SpotifyApi {

	@GET("me")
	Call<SpotifyMe> getMe();

	@GET("users/{id}/playlists")
	Call<GetSpotifyPlaylistsResponse> getPlaylists(
			@Path("id") String id,
			@Query("limit") int limit
	);

	@GET
	Call<GetSpotifyPlaylistsResponse> getPlaylists(
			@Url String url
	);

	@GET("users/{user_id}/playlists{playlist_id}/tracks")
	Call<GetSpotifyPlaylistTracksResponse> getPlaylistTracks(
			@Path("user_id") String userId,
	        @Path("playlist_id") String playlistId,
	        @Query("offset") int offset,
	        @Query("limit") int limit
	);

	@GET
	Call<GetSpotifyPlaylistTracksResponse> getPlaylistTracks(
			@Url String url
	);

}
