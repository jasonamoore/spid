package iterator;

import java.util.NoSuchElementException;

import playlist.Playlist;
import playlist.Song;

/**
 * The OrderedPlaylistIterator defines the iteration functions for all
 * PlaylistIterators which traverse based on a sorted order.
 * 
 * @author Jason Moore
 */
public abstract class OrderedPlaylistIterator extends PlaylistIterator {

	protected SortedSongLinkedList sortlist;
	protected SongNode current;
	protected boolean ascending;

	/**
	 * Creates a PlaylistIterator given a {@link Playlist} and
	 * a boolean representing the order in which to traverse.
	 * 
	 * @param playlist The playlist to iterate over.
	 * @param ascending If the playlist should be sorted in ascending order.
	 */
	protected OrderedPlaylistIterator(Playlist playlist, boolean ascending) {
		super(playlist);
		sortlist = new SortedSongLinkedList();
		this.ascending = ascending;
		current = ascending ? sortlist.getHead() : sortlist.getTail();
	}

	@Override
	public boolean hasPrevious() {
		SongNode prev = ascending ? current.getPrev() : current.getNext();
		return sortlist.isNode(prev);
	}

	@Override
	public boolean hasNext() {
		SongNode next = ascending ? current.getNext() : current.getPrev();
		return sortlist.isNode(next);
	}

	@Override
	public Song previous() {
		if (hasPrevious())
			// gets the song at index of the prev node and moves current
			return songs[(current = ascending ? current.getPrev() : current.getNext()).getId()];
		else
			throw new NoSuchElementException("No previous element in playlist");
	}

	@Override
	public Song next() {
		if (hasNext()) {
			// gets the song at the current index to return, then moves current the the next
			return songs[(current = ascending ? current.getNext() : current.getPrev()).getId()];
		} else
			throw new NoSuchElementException("No next element in playlist");

	}

	protected abstract int compare(Song a, Song b);

	/**
	 * A wrapped class which acts as a helper for sorting a collection of songs
	 * based on comparison of the iterator's sorting key. Constructs a sorted doubly
	 * linked list of SongNodes.
	 * 
	 * @author Jason Moore
	 */
	class SortedSongLinkedList {

		private SongNode head;
		private SongNode tail;

		/**
		 * Created an empty list with a sentinel head and tail node. Calls
		 * {@link #sortForIterator} to insert songs from playlist.
		 */
		private SortedSongLinkedList() {
			head = new SongNode(-1, null, null); // sentinel head node
			tail = new SongNode(-1, head, null); // sentinel tail node
			head.setNext(tail); // connect
			sortForIterator();
		}

		/**
		 * Using the songs array, inserts all Songs into the linked list in sorted
		 * order. This is accomplished by using the iterator's compare method to compare
		 * two songs based on the sorting key.
		 */
		public void sortForIterator() {
			for (int i = 0; i < songs.length; i++) {
				Song song = songs[i];
				SongNode cursor = head.getNext();
				for (; isNode(cursor); cursor = cursor.getNext())
					if (OrderedPlaylistIterator.this.compare(song, songs[cursor.getId()]) < 0)
						break;
				SongNode past = cursor.getPrev();
				SongNode future = cursor;
				SongNode present = new SongNode(i, past, future);
				past.setNext(present);
				future.setPrev(present);
			}
		}

		/**
		 * Checks if a node is a real data entry in the list.
		 * 
		 * @param node The node to check.
		 * @return True if the node is not null or a sentinel (head or tail) node.
		 */
		public boolean isNode(SongNode node) {
			return node != null && node.getId() >= 0; // node must be not null and not a sentinel node
		}

		/**
		 * Returns the sentinel head node of the list.
		 * 
		 * @return The head node.
		 */
		public SongNode getHead() {
			return head;
		}

		/**
		 * Returns the sentinel tail node of the list.
		 * 
		 * @return The tail node.
		 */
		public SongNode getTail() {
			return tail;
		}

	}

	/**
	 * A wrapped class used by the {@link SortedSongLinkedList} class which consists
	 * of two SongNodes links (a previous and next node), and a single integer
	 * representing an index in the songs array.
	 * 
	 * @author Jason Moore
	 */
	class SongNode {

		int id;
		SongNode prev;
		SongNode next;

		/**
		 * Creates a node with a given id, previous, and next.
		 * 
		 * @param id   The index of the song this node represents.
		 * @param prev A link to the previous node.
		 * @param next A link to the next node.
		 */
		public SongNode(int id, SongNode prev, SongNode next) {
			this.id = id;
			this.prev = prev;
			this.next = next;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setPrev(SongNode prev) {
			this.prev = prev;
		}

		public void setNext(SongNode next) {
			this.next = next;
		}

		public int getId() {
			return id;
		}

		public SongNode getPrev() {
			return prev;
		}

		public SongNode getNext() {
			return next;
		}

	}

}