package iterator;

import java.util.NoSuchElementException;

import playlist.Playlist;
import playlist.Song;

public class ShuffleIterator extends PlaylistIterator {

	private boolean[] shuffleHistory;
	private int[] shuffleOrder;
	private int shuffleIndex;

	/**
	 * Creates a new iterator for shuffling the playlist. Initializes shuffleHistory
	 * and shuffleOrder arrays, which are used to keep track of which songs have
	 * been picked and in what order. shuffleIndex is initialized to -1, indicating
	 * that no songs have been picked yet.
	 * 
	 * @param playlist
	 */
	public ShuffleIterator(Playlist playlist) {
		super(playlist);
		shuffleHistory = new boolean[playlist.getSize()];
		shuffleOrder = new int[playlist.getSize()];
		shuffleIndex = -1;
		for (int i = 0; i < shuffleOrder.length; i++)
			shuffleOrder[i] = -1;
	}

	@Override
	public boolean hasPrevious() {
		return shuffleIndex > 0;
	}

	@Override
	public boolean hasNext() {
		return shuffleIndex < playlist.getSize() - 1;
	}

	@Override
	public Song previous() {
		if (hasPrevious())
			return songs[shuffleOrder[--shuffleIndex]]; // decrement shuffleIndex before returning
		else
			throw new NoSuchElementException("No previous element in playlist");
	}

	@Override
	public Song next() {
		if (hasNext()) {
			++shuffleIndex; // next index
			if (shuffleOrder[shuffleIndex] == -1) {
				shuffleOrder[shuffleIndex] = pickNext();
			}
			return songs[shuffleOrder[shuffleIndex]]; // increment shuffleIndex after returning
		} else
			throw new NoSuchElementException("No previous element in playlist");
	}

	/**
	 * Picks a random integer, representing the index of a song in the songs array,
	 * which has not yet been picked in the shuffle list. Marks the new integer as
	 * used.
	 * 
	 * @return An integer previously unused in the shuffleOrder array.
	 */
	private int pickNext() {
		int id;
		do {
			id = (int) Math.floor(Math.random() * playlist.getSize());
		} while (shuffleHistory[id]);
		shuffleHistory[id] = true;
		return id;
	}

}
