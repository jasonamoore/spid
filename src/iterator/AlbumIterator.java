package iterator;

import playlist.Playlist;
import playlist.Song;

public class AlbumIterator extends OrderedPlaylistIterator {

	public AlbumIterator(Playlist playlist, boolean ascending) {
		super(playlist, ascending);
	}
	
	/**
	 * Compares two songs based on the tracks' album names.
	 * 
	 * @return The result of comparing the two album names.
	 */
	@Override
	protected int compare(Song a, Song b) {
		return a.getAlbum().toLowerCase().compareTo(b.getAlbum().toLowerCase());
	}

}
