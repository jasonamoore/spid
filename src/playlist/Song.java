package playlist;

import java.time.LocalDateTime;

import json.JsonArray;
import json.JsonInteger;
import json.JsonObject;
import json.JsonString;

/**
 * A simple data structure for holding information about a song, including track
 * title, primary artist name, album title, date added to the playlist, and the
 * duration in milliseconds of the track.
 * 
 * @author Jason
 */
public class Song {

	private LocalDateTime addedDate;
	private String trackTitle;
	private String albumTitle;
	private String artistName;
	private int duration;

	public Song(JsonObject json) {
		loadSong(json);
	}

	/**
	 * Extracts data from a JSON object representing this song and loads data fields
	 * accordingly.
	 * 
	 * @param json The JSON object representing this track.
	 */
	private void loadSong(JsonObject json) {
		JsonString added_at = (JsonString) json.get("added_at");
		String date_str = added_at.getValue();
		addedDate = LocalDateTime.parse(date_str.substring(0, date_str.length() - 1));
		JsonObject track = (JsonObject) json.get("track");
		JsonString track_name = (JsonString) track.get("name");
		trackTitle = track_name.getValue();
		JsonObject album = (JsonObject) track.get("album");
		JsonString album_name = (JsonString) album.get("name");
		albumTitle = album_name.getValue();
		JsonArray artists = (JsonArray) track.get("artists");
		JsonString artist_name = (JsonString) ((JsonObject) artists.get(0)).get("name");
		artistName = artist_name.getValue();
		JsonInteger dur_ms = (JsonInteger) track.get("duration_ms");
		duration = dur_ms.getValue();
	}

	public String getArtist() {
		return artistName;
	}

	public LocalDateTime getDateAdded() {
		return addedDate;
	}

	public String getTitle() {
		return trackTitle;
	}

	public String getAlbum() {
		return albumTitle;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		final int MS_PER_MIN = 60_000;
		int minutes = duration / MS_PER_MIN;
		int seconds = (int) Math.round((duration - (minutes * MS_PER_MIN)) / 1000.0);
		return String.format("%s by %s\n\ton %s\n\tadded: %s, duration: %d:%02d", trackTitle, artistName, albumTitle,
				addedDate, minutes, seconds);
	}

}
