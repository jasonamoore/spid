HELP
- To set up this demo, you will need to connect to your own
  Spotify API project, which you can set up for free at
  developer.spotify.com. Set up a "development mode" project,
  and Spotify will give you a client ID and client secret
  needed to authorize API requests.
- To link those login tokens with this demo, you will need
  to provide a file containing the ID and secret. The format
  for this file should be the following:
  """
  <clientID>
  <clientSecret>
  """
  That is, the client ID, a new line, then the client secret.
  You may provide a path to your login file, or by default
  SPID will look for a file named "tokens.txt".

RUNNING
- You can run the prebuilt JAR file using this command:
    java -jar spid.jar <token-filepath(optional)>
- Or you can use the makefile to compile and run the demo:
    make compile
    make demo
  (Note: you can modify the makefile to run with arguments)

DOCUMENTATION
- The Spotify Playlist Iterator Demo demonstrates the Iterator
  design pattern using playlists as an iterable data structure.
- SPID sends a GET request to retrieve a JSON response with
  playlist data, including a list of tracks in the playlist.
- The JsonParser class handles decoding the API response and
  providing a JsonObject from which data can be accessed.
- Using the "tracks" object, a Playlist can be instantiated
  containing an array of Songs representing each music track.
- There are six iterators provided for traversing a playlist,
  which include ordered iterators and the shuffle iterator.
- The demo provides an interface for loading playlists from
  a URL, viewing loaded playlists, and performing iteration
  methods on a playlist. Using the demo, any of the six
  iterators can be chosen and tested using their hasPrevious,
  hasNext, previous, and next methods by displaying the result.

CREDITS
~
SPID version 1.1.0.
All code written by Jason Moore.
All code is free and open source.
~
SPID uses the Spotify Web API to download playlist data.
Spotify Web API documentation can be found at:
	https://developer.spotify.com/documentation/web-api
~
Special thanks to Linode for a Spotify Web API tutorial:
	https://www.youtube.com/watch?v=WAmEZBEeNmg
~
