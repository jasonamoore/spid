package iterator;
import playlist.Playlist;
import playlist.Song;

public class DurationIterator extends OrderedPlaylistIterator {

	public DurationIterator(Playlist playlist, boolean ascending) {
		super(playlist, ascending);
	}
	
	/**
	 * Compares two songs based on the duration of the track.
	 * 
	 * @return The result of comparing the tracks' durations.
	 */
	@Override
	protected int compare(Song a, Song b) {
		return a.getDuration() - b.getDuration();
	}

}
