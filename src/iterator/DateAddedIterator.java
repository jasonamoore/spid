package iterator;

import playlist.Playlist;
import playlist.Song;

public class DateAddedIterator extends OrderedPlaylistIterator {

	public DateAddedIterator(Playlist playlist, boolean ascending) {
		super(playlist, ascending);
	}

	/**
	 * Compares two songs based on the date added to the playlist.
	 * 
	 * @return The result of comparing the two dates.
	 */
	@Override
	protected int compare(Song a, Song b) {
		return a.getDateAdded().compareTo(b.getDateAdded());
	}

}
