package iterator;

import playlist.Playlist;
import playlist.Song;

public class ArtistIterator extends OrderedPlaylistIterator {

	public ArtistIterator(Playlist playlist, boolean ascending) {
		super(playlist, ascending);
	}
	
	/**
	 * Compares two songs based on the tracks' primary artists.
	 * 
	 * @return The result of comparing the two artist names.
	 */
	@Override
	protected int compare(Song a, Song b) {
		return a.getArtist().toLowerCase().compareTo(b.getArtist().toLowerCase());
	}

}
