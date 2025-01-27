package playlist;

/**
 * A simple data structure for holding information about a playlist, including
 * name, list of tracks, and Spotify playlist ID.
 * 
 * @author Jason
 */
public class Playlist {

	private String id;
	private String name;
	private Song[] songs;

	public Playlist(String id, String name, Song[] songs) {
		this.id = id;
		this.name = name;
		this.songs = songs;
	}

	public Song[] getSongs() {
		return songs;
	}

	public int getSize() {
		return songs.length;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("\"%s\", %d tracks\n", getName(), getSize());
	}

}