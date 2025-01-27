package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import iterator.PlaylistIterator;
import iterator.AlbumIterator;
import iterator.ArtistIterator;
import iterator.DateAddedIterator;
import iterator.DurationIterator;
import iterator.ShuffleIterator;
import iterator.TitleIterator;
import playlist.AuthorizationException;
import playlist.Playlist;
import playlist.PlaylistLoadException;
import playlist.PlaylistLoader;

/**
 * Runnable demo class for SPID.
 * 
 * @author Jason Moore
 */
public class Demo {

	private static ArrayList<Playlist> loadedPlaylists;
	private static final String aboutPath = "README.txt";
	private static final String defaultAuthPath = "tokens.txt";

	private static String clientId, clientSecret;

	public static String getClientId() {
		return clientId;
	}
	
	public static String getClientSecret() {
		return clientSecret;
	}

	/**
	 * The main method for the demo class, which runs the SPID demo.
	 * 
	 * @param args Command-line arguments, which may provide a path to a file
					containing the user's API login (client ID and secret).
	 */
	public static void main(String[] args) {
		// try to read tokens from file
		if (args.length < 2) {
			String tkPath = args.length > 0 ? args[0] : defaultAuthPath;
			File tkFile = new File(tkPath);
			if (!tkFile.exists() || !tkFile.isFile()) {
				System.out.printf("<!> The login file '%s' does not exist!\n", tkPath);
				System.out.println("Refer to the README for help setting up this demo.");
				System.exit(1);
			}
			else {
				try {
					Scanner tks = new Scanner(tkFile);
					clientId = tks.nextLine();
					clientSecret = tks.nextLine();
					tks.close();
					if (clientId.equals("<yourClientIdHere>")) {
						System.out.println("<!> Did you forget to update the login file with your credentials?");
						System.exit(1);
					}
				} catch (NoSuchElementException nse) {
					System.out.printf("<!> The login file '%s' is improperly formatted!\n", tkPath);
					System.out.println("The file should contain two lines: the client ID and client secret.");
					System.exit(1);
				} catch (FileNotFoundException e) {
					// shouldn't happen since file.exists() was checked
					System.exit(1);
				}
			}
		}
		else {
			System.out.println("<!> Too many arguments!");
			System.out.println("Refer to the README for help setting up this demo.");
			System.exit(1);
		}
		System.out.print("** This demo requires an internet connection to run **\nConnecting... ");
		loadedPlaylists = new ArrayList<Playlist>();
		try {
			PlaylistLoader.authorize();
		} catch (AuthorizationException e) {
			System.out.println("<!> Error authorizing client. Check that your login file has the right client ID and secret.");
			System.exit(1);
		} catch (IOException | InterruptedException e) {
			System.out.println("<!> Error connecting. Check internet connection and try again.");
			System.exit(1);
		}
		System.out.println("Connected.");
		//
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the Spotify Playlist Iterator Demo (SPID)!");
		boolean running = true;
		while (running) {
			int choice = 0;
			do {
				System.out.println(
						"What would you like to do?\n\t1. Load a playlist\n\t2. View loaded playlists\n\t3. Iterate over a playlist\n\t4. README & Credits\n\tQ. Quit");
				String line = in.nextLine();
				if (line.matches("1|2|3|4"))
					choice = Integer.parseInt(line);
				else if (line.length() > 0 && line.toUpperCase().charAt(0) == 'Q')
					choice = -1;
				else
					System.out.println("Please enter a valid choice.");
			} while (choice == 0);
			if (choice == 1) {
				System.out.println(
						"To load a playlist, input the link provided in Spotify under 'Share > Copy link to playlist'.");
				System.out.println("Please input a Spotify public playlist URL: ");
				try {
					String url = in.nextLine();
					String pid = PlaylistLoader.grabPlaylistId(url);
					boolean duplicate = false;
					for (int i = 0; i < loadedPlaylists.size(); i++)
						duplicate |= loadedPlaylists.get(i).getId().equals(pid);
					if (!duplicate) {
						System.out.printf("Loading playlist [id = %s]... ", pid);
						Playlist newp = PlaylistLoader.loadPlaylist(url);
						System.out.println("Successfully loaded.");
						System.out.printf("Playlist details:\n\t%s\n", newp.toString());
						loadedPlaylists.add(newp);
					} else
						System.out.println("This playlist has already been loaded.");
				} catch (PlaylistLoadException e) {
					System.out.println(
							"Error loading playlist. Ensure that the playlist is public and the URL is not malformed.");
				}
			} else if (choice == 2) {
				if (loadedPlaylists.size() == 0)
					System.out.println("No playlists are loaded.");
				else {
					System.out.println("Loaded playlists:");
					for (int i = 0; i < loadedPlaylists.size(); i++) {
						Playlist p = loadedPlaylists.get(i);
						System.out.printf("%d:\t%s\n", i + 1, p.toString());
					}
				}
			} else if (choice == 3) {
				if (loadedPlaylists.size() == 0)
					System.out.println("No playlists are loaded.");
				else {
					int pid = -1;
					do {
						System.out.printf("Which playlist would you like to iterate over? [1-%d]\n",
								loadedPlaylists.size());
						String pinp = in.nextLine();
						if (pinp.matches("[0-9]+")) {
							pid = Integer.parseInt(pinp);
							if (pid <= 0 || pid > loadedPlaylists.size()) {
								pid = -1;
								System.out.printf("Please enter a valid choice between 1 and %d.\n",
										loadedPlaylists.size());
							}
						} else
							System.out.printf("Please enter a valid choice between 1 and %d.\n",
									loadedPlaylists.size());
					} while (pid < 0);
					int iterid = -1;
					final int num_iters = 6;
					do {
						System.out.printf("Which iterator would you like to use? [1-%d]\n", num_iters);
						System.out.println(
								"\t1. AlbumIterator\n\t2. ArtistIterator\n\t3. DateAddedIterator\n\t4. DurationIterator\n\t5. ShuffleIterator\n\t6. TitleIterator");
						String inp = in.nextLine();
						if (inp.matches("[0-9]+")) {
							iterid = Integer.parseInt(inp);
							if (iterid <= 0 || iterid > 6) {
								iterid = -1;
								System.out.printf("Please enter a valid choice between 1 and %d.\n", num_iters);
							}
						} else
							System.out.printf("Please enter a valid choice between 1 and %d.\n", num_iters);
					} while (iterid < 0);
					int ascending = -1;
					if (iterid != 5) { // shuffle does not need order
						do {
							System.out.println("Iterate in ascending or descending order? [A/D]");
							String ad_str = in.nextLine().toUpperCase();
							char ad = ad_str.length() > 0 ? ad_str.charAt(0) : 'x';
							if (ad == 'A')
								ascending = 1;
							else if (ad == 'D')
								ascending = 0;
							else
								System.out.println("Please enter a valid choice.");
						} while (ascending < 0);
					}
					Playlist chosen = loadedPlaylists.get(pid - 1);
					PlaylistIterator iter = getIterByNum(iterid - 1, chosen, ascending == 1);
					boolean iterating = true;
					int last_method = 0;
					while (iterating) {
						int method = 0;
						do {
							System.out.println(
									"What method would you like to call?\n(Press enter to repeat last method, type Q to quit)");
							System.out.println("\t1. hasPrevious\n\t2. hasNext\n\t3. previous\n\t4. next");
							String minp = in.nextLine();
							if (minp.matches("1|2|3|4"))
								method = Integer.parseInt(minp);
							else if (minp.length() > 0 && minp.toUpperCase().charAt(0) == 'Q')
								method = -1;
							else if (minp.length() == 0) {
								if (last_method == 0)
									System.out.println("No method has been used yet.");
								else
									method = last_method;
							} else
								System.out.println("Please enter a valid choice.");
						} while (method == 0);
						if (method == 1) {
							System.out.printf("hasPrevious returned: %b\n\n", iter.hasPrevious());
						} else if (method == 2) {
							System.out.printf("hasNext returned: %b\n\n", iter.hasNext());
						} else if (method == 3) {
							if (iter.hasPrevious())
								System.out.printf("previous returned:\n%s\n\n", iter.previous());
							else
								System.out.println("Iterator does not have a previous item.");
						} else if (method == 4) {
							if (iter.hasNext())
								System.out.printf("next returned:\n%s\n\n", iter.next());
							else
								System.out.println("Iterator does not have a next item.");
						} else
							iterating = false;
						last_method = method;
					}
				}
			} else if (choice == 4) {
				try {
					Scanner abt = new Scanner(new File(aboutPath));
					while (abt.hasNextLine())
						System.out.println(abt.nextLine());
				} catch (IOException e) {
					System.out.println("Error occurred while loading README & Credits text.");
				}
			} else
				running = false;
		}
		in.close();
	}

	/**
	 * Creates an iterator for a playlist given a number corresponding to its type.
	 * 
	 * @param n        The number of the iterator to create.
	 * @param playlist The playlist to be used by the iterator.
	 * @param ascend   If the iterator should traverse in ascending order. Unused
	 *                 for shuffle iterator.
	 * @return The chosen iterator.
	 */
	private static PlaylistIterator getIterByNum(int n, Playlist playlist, boolean ascend) {
		switch (n) {
		case 0:
			return new AlbumIterator(playlist, ascend);
		case 1:
			return new ArtistIterator(playlist, ascend);
		case 2:
			return new DateAddedIterator(playlist, ascend);
		case 3:
			return new DurationIterator(playlist, ascend);
		case 4:
			return new ShuffleIterator(playlist);
		case 5:
			return new TitleIterator(playlist, ascend);
		default:
			return null;
		}
	}

}
