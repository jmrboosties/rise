package fitness.classmate.net.gson;

import com.google.gson.*;
import fitness.classmate.constant.FieldNames;
import fitness.classmate.model.SpotifyPlaylistTrack;

import java.lang.reflect.Type;

public class SpotifyPlaylistTrackDeserializer implements JsonDeserializer<SpotifyPlaylistTrack> {

	@Override
	public SpotifyPlaylistTrack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Gson gson = GsonFactory.createGson(SpotifyPlaylistTrack.class);

		SpotifyPlaylistTrack track = gson.fromJson(json, SpotifyPlaylistTrack.class);

		JsonObject trackJson = json.getAsJsonObject().getAsJsonObject(FieldNames.TRACK);

		JsonArray artists = trackJson.getAsJsonObject().getAsJsonArray(FieldNames.ARTISTS);
		track.setArtist(artists.get(0).getAsJsonObject().getAsJsonPrimitive(FieldNames.NAME).getAsString());

		track.setName(trackJson.getAsJsonPrimitive(FieldNames.NAME).getAsString());
		track.setDuration(trackJson.getAsJsonPrimitive(FieldNames.DURATION_MS).getAsLong());
		track.setUri(trackJson.getAsJsonPrimitive(FieldNames.URI).getAsString());

		JsonArray images = trackJson.getAsJsonObject(FieldNames.ALBUM).getAsJsonArray(FieldNames.IMAGES);
		track.setImageUrl(images.get(0).getAsJsonObject().getAsJsonPrimitive(FieldNames.URL).getAsString());

		return track;
	}

}
