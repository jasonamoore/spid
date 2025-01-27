package playlist;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import demo.Demo;
import json.JsonArray;
import json.JsonInteger;
import json.JsonObject;
import json.JsonParser;
import json.JsonString;
import json.JsonType;

public class PlaylistLoader {

	private static final String AUTH_URL = "https://accounts.spotify.com/api/token";
	private static final String AUTH_FORMAT = "grant_type=client_credentials&client_id=%s&client_secret=%s";

	private static final String PLAYLIST_URL_FORMAT = "https://api.spotify.com/v1/playlists/%s";

	private static String authToken;
	private static long expiryTime;

	/**
	 * Retrieves an authorization token from the Spotify Web API.
	 * <p>
	 * If no authorization token has been retrieved yet, or the current
	 * authorization token has expired, an authorization request will be sent and
	 * the response parsed to extract the token.
	 * @param clientSecret 
	 * @param clientId 
	 * 
	 * @return A String containing the current or new authorization key.
	 * @throws AuthorizationException If an authorization token could not be
	 *                                retrieved.
	 * @throws IOException            If the HTTP connection fails.
	 */
	public static String authorize() throws AuthorizationException, IOException, InterruptedException {
		if (authToken != null && System.currentTimeMillis() < expiryTime)
			return authToken;
		// create connection
        HttpClient client = HttpClient.newHttpClient();
		// client data being sent to auth url
		String authBody = String.format(AUTH_FORMAT, Demo.getClientId(), Demo.getClientSecret());
		// POST request to send client data
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(authBody, StandardCharsets.UTF_8))
                .build();
        // send request (an exception here will be caught by demo as a connection failure)
        HttpResponse<String> httpresponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		// any response code other than 200 indicates auth failure
        if (httpresponse.statusCode() != 200)
			throw new AuthorizationException("Authorization request failed");
        // read response body into JSON
		JsonObject response = (JsonObject) JsonParser.read(httpresponse.body().toString());
		JsonType access_token = response.get("access_token");
		if (access_token.isNull())
			throw new AuthorizationException("Could not retreive authentication token");
		JsonString token = (JsonString) access_token;
		authToken = token.getValue();
		JsonInteger expires_in = (JsonInteger) response.get("expires_in");
		expiryTime = System.currentTimeMillis() + expires_in.getValue() * 1000;
		return authToken;
	}

	/**
	 * Loads a playlist from its shareable URL using the Spotify Web API.
	 * <p>
	 * Creates a new GET request to the Spotify Web API to retreive a JSON reponse
	 * containing data about a playlist and its list of tracks.
	 * 
	 * @param url The URL pointing to the Spotify playlist.
	 * @return A Playlist object constructed using data from the API response.
	 * @throws PlaylistLoadException If communication with the Spotify API fails.
	 */
	public static Playlist loadPlaylist(String url) throws PlaylistLoadException {
		try {
			authToken = authorize();
		} catch (AuthorizationException e) {
			throw new PlaylistLoadException("Error authenticating Spotify API token");
		} catch (IOException | InterruptedException e) {
			throw new PlaylistLoadException("Error connecting to Spotify API");
		}
		try {
			// initial data
			String playlist_id = grabPlaylistId(url);
			Object getUrl = String.format(PLAYLIST_URL_FORMAT, playlist_id);
			int song_index = 0;
			int total = 0;
			String name = "";
			Song[] songs = null;
			do {
				// create connection
				HttpClient client = HttpClient.newHttpClient();
				// set request properties
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create((String) getUrl))
		                .header("Authorization", "Bearer " + authToken)
		                .GET()
		                .build();
		        // send request (an exception here will be caught and thrown as a connection failure)
		        HttpResponse<String> httpresponse;
				try {
					httpresponse = client.send(request, HttpResponse.BodyHandlers.ofString());
				} catch (InterruptedException e) {
					throw new PlaylistLoadException("Error connecting to Spotify API");
				}
				// any response code other than 200 indicates failure
		        if (httpresponse.statusCode() != 200)
					throw new PlaylistLoadException("Error loading playlist URL");
		        // read response body into JSON
		        System.out.println(httpresponse.body());
				JsonObject response = (JsonObject) JsonParser.read(httpresponse.body());
				// only the first GET will contain playlist data; subsequent loops only get the
				// "tracks" object, so response object is the "tracks" object
				JsonObject tracks = song_index > 0 ? response : (JsonObject) response.get("tracks");
				JsonArray items = (JsonArray) tracks.get("items");
				// initialize song array if not already
				if (songs == null) {
					total = ((JsonInteger) tracks.get("total")).getValue();
					name = ((JsonString) response.get("name")).getValue();
					songs = new Song[total];
				}
				// for each object in "tracks" list, add new Song
				for (int i = 0; i < items.size(); i++, song_index++) {
					songs[song_index] = new Song((JsonObject) items.get(i));
				}
				// if the data requires another GET, update the next URL
				getUrl = tracks.get("next").getValue();
			} while (getUrl != null);
			// return a new playlist constructed with the Song array
			return new Playlist(playlist_id, name, songs);
		} catch (IOException e) {
			throw new PlaylistLoadException("Error finding playlist");
		} catch (ClassCastException e) {
			throw new PlaylistLoadException("Unexpected response from Spotify API");
		}
	}

	/**
	 * Reads a shareable Spotify playlist link and extracts the playlist identifier.
	 * 
	 * @param url The URL to be parsed.
	 * @return The playlist ID.
	 */
	public static String grabPlaylistId(String url) {
		if (!url.contains("spotify") || !url.contains("playlist"))
			return null;
		String id = url.substring(url.lastIndexOf("/") + 1);
		if (id.contains("?"))
			id = id.substring(0, id.indexOf("?"));
		return id;
	}

}