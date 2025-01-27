package iterator;

import playlist.Playlist;
import playlist.Song;

public class TitleIterator extends OrderedPlaylistIterator {

	public TitleIterator(Playlist playlist, boolean ascending) {
		super(playlist, ascending);
	}
	
	/**
	 * Compares two songs based on the tracks' titles.
	 * 
	 * @return The result of comparing the two titles.
	 */
	@Override
	protected int compare(Song a, Song b) {
		return a.getTitle().toLowerCase().compareTo(b.getTitle().toLowerCase());
	}

}
