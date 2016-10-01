package fitness.classmate.net.gson;

import com.google.gson.*;
import fitness.classmate.constant.FieldNames;
import fitness.classmate.model.SpotifyPlaylist;

import java.lang.reflect.Type;

public class SpotifyPlaylistDeserializer implements JsonDeserializer<SpotifyPlaylist> {

	@Override
	public SpotifyPlaylist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Gson gson = GsonFactory.createGson(SpotifyPlaylist.class);

		SpotifyPlaylist spotifyPlaylist = gson.fromJson(json, SpotifyPlaylist.class);

		JsonObject tracks = json.getAsJsonObject().getAsJsonObject(FieldNames.TRACKS);

		spotifyPlaylist.setTrackCount(tracks.getAsJsonPrimitive(FieldNames.TOTAL).getAsInt());
		spotifyPlaylist.setTracksUrl(tracks.getAsJsonPrimitive(FieldNames.HREF).getAsString());

		return spotifyPlaylist;
	}

}
