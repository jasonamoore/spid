package iterator;

import playlist.Playlist;
import playlist.Song;

/**
 * The PlaylistIterator class declares the functions and fields which every
 * iterator must use.
 * 
 * @author Jason
 */
public abstract class PlaylistIterator {

	Playlist playlist;
	Song[] songs;

	/**
	 * Creates a PlaylistIterator given a {@link Playlist}.
	 * 
	 * @param playlist The playlist to iterate over.
	 */
	protected PlaylistIterator(Playlist playlist) {
		this.playlist = playlist;
		songs = playlist.getSongs();
	}

	/**
	 * Checks whether the iterator has a previous item to return.
	 * 
	 * @return True if the iterator has a previous, otherwise false.
	 */
	public abstract boolean hasPrevious();

	/**
	 * Checks whether the iterator has a next item to return.
	 * 
	 * @return True if the iterator has a next, otherwise false.
	 */
	public abstract boolean hasNext();

	/**
	 * Iterates backwards to the previous item traversed.
	 * 
	 * @return The previous Song in the playlist.
	 * @throws {@link java.util.NoSuchElementException} if there is no previous item
	 *                to retrieve.
	 */
	public abstract Song previous();

	/**
	 * Iterates forwards to the next item to traverse.
	 * 
	 * @return The next Song in the playlist.
	 * @throws {@link java.util.NoSuchElementException} if there is no next item to
	 *                retrieve.
	 */
	public abstract Song next();

}
