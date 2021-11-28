package jukeBox;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.sound.sampled.*;

public class Song {

	public Song() {
	}

	static List<Song> newList = new ArrayList<Song>();
	static List<Song> list = new ArrayList<Song>();
	static Scanner sc = new Scanner(System.in);
	static Long currentFrame;
	static String stat = null;
	static String url;
	static PlayList playlist = new PlayList();

	private String songId; //SID1001
	private String songName;
	private String artist;
	private String genre;
	private String album;
	private String duration;

	public Song(String songId, String songName, String artist, String genre, String album, String duration) {
		super();
		this.songId = songId;
		this.songName = songName;
		this.artist = artist;
		this.genre = genre;
		this.album = album;
		this.duration = duration;
	}
	// display all songs and search by name,artist,genre,album
	public int searchSong(Statement smt) throws Exception {
		list.clear();
		newList.clear();
		boolean done = false;
		try {
			String query = "select * from song";
			ResultSet rs1 = smt.executeQuery(query);
			if(!rs1.next()) {
				System.out.println("No Records Found");
				return 0;
			}
			ResultSet rs = smt.executeQuery(query);
			while(rs.next()) {
				Song song = new Song(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
				list.add(song);						// adding all song objects to a list from database
			}
			while(!done) {
				System.out.println("1.Display all  2.Search By Song Name  3.Search By Artist  4.Search By Genre  5.Search By Album  6.Exit");
				int opt = sc.nextInt();
				switch (opt) {
				case 1: {
					newList.addAll(list);
					if(!newList.isEmpty()) {
						display(newList);			//calling method to display the songs
						done=true;
						break;
					}
					System.out.println("No records found !");
					break;
				}
				case 2: {
					String search = search();
					list.stream().forEach((o)-> {	//used lambda exp. to filter songs as required using regular-Exp
						if(o.songName.toLowerCase().matches(".*"+search.toLowerCase()+".*")) {
							newList.add(o);
						}
					});
					if(!newList.isEmpty()) {
						display(newList);
						done=true;
						break;
					}
					System.out.println("No records found !");
					break;
				}
				case 3: {
					String search = search();
					list.stream().forEach((o)-> {
						if(o.artist.toLowerCase().matches(".*"+search.toLowerCase()+".*")) {
							newList.add(o);
						}
					});
					if(!newList.isEmpty()) {
						display(newList);
						done=true;
						break;
					}
					System.out.println("No records found !");
					break;
				}
				case 4: {
					String search = search();
					list.stream().forEach((o)-> {
						if(o.genre.toLowerCase().matches(".*"+search.toLowerCase()+".*")) {
							newList.add(o);
						}
					});
					if(!newList.isEmpty()) {
						display(newList);
						done=true;
						break;
					}
					System.out.println("No records found !");
					break;
				}
				case 5: {
					String search = search();
					list.stream().forEach((o)-> {
						if(o.album.toLowerCase().matches(".*"+search.toLowerCase()+".*")) {
							newList.add(o);
						}
					});
					if(!newList.isEmpty()) {
						display(newList);
						done=true;
						break;
					}
					System.out.println("No records found !");
					break;
				}
				case 6:{
					done = true;
					return 6;
				}
				default:
					System.out.println("Invalid option selected !\n");
					break;
				}
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid option selected !");
		}
		return 1;
	}
	// display songs
	public void display(List<Song> newList) {
		System.out.format("%3s %25s %25s %10s %25s %9s","SL#","Song","Artist","Genre","Album","Duration");
		System.out.print("\n/------------------------------------------------------------------------------------------------------/");
		newList.stream().forEach((o)->{			
			System.out.format("\n%3s %25s %25s %10s %25s %9s",newList.indexOf(o)+1,o.songName,o.artist,o.genre,o.album,o.duration);
		});
		System.out.println("\n/------------------------------------------------------------------------------------------------------/");

	}
	//search input by user
	private String search() {
		System.out.println("Search song ->");
		String search =new Scanner(System.in).nextLine();
		return 	search;
	}
	//For playing songs
	public void playSong(String userId, Statement smt, String select) throws Exception {
		boolean done = false;
			if(select.equals("song")) {
				if(searchSong(smt)==6) return;
			}
			if(select.equals("playlist")) {
				if(playlist.play(userId, smt).equals("Empty Playlist !")) {
					System.out.println("No playlist Available !");
					return;
				}
			}
			while(!done) {
				try {
					System.out.println("\nEnter song index to play or go back(0)");
					int i = sc.nextInt();
					if(i==0 || i>newList.size()) throw new InvalidChoiceException("Invalid Choice !");
					songName = newList.get(i-1).songName;
					player();							// calling method to play songs
				}catch(InputMismatchException e) {
					System.out.println("Invalid option selected !");
					continue;
				}catch(InvalidChoiceException e) {
					System.out.println(e.getMessage());
					continue;
				}
			}
	}
	//song player
	private void player() throws Exception {
		Clip clip = AudioSystem.getClip();
		url = "C:\\Users\\Shreyasi\\Desktop\\JukeBoxAudio\\"+songName+".wav";
		resetAudioStream(clip);						// initialize and reset the audio input stream
		play(clip);
		while (true) {
			try {
			System.out.println("1. Pause  2. Resume  3. Restart  4. Stop");
			int choice = sc.nextInt();
			if(choice<1 || choice>4) throw new InvalidChoiceException("Invalid Choice !");
			Choice(choice,clip);
			if (choice==4) break;
			}catch(InvalidChoiceException e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
		
	}
	//playing the clip
	private void play(Clip clip) {
		clip.start();
		stat = "Playing...";
		System.out.println(stat);
	}
	//method to manipulate the audio according to the user 
	private void Choice(int choice, Clip clip) throws Exception {
		switch (choice) 
		{
		case 1:
			if(stat.equals("Paused !")) {
				System.out.println("Audio is already Paused !");
				break;
			}
			currentFrame = clip.getMicrosecondPosition(); // getting current position of the audio file
			clip.stop();
			stat = "Paused !";
			System.out.println(stat);
			break;
		case 2:
			if(stat.equals("Playing...")) {
				System.out.println("Audio is already Playing !");
				break;
			}
			clip.close();
			resetAudioStream(clip);
			clip.setMicrosecondPosition(currentFrame); // setting current position of the audio file
			play(clip);
			break;
		case 3:
			clip.stop();
			clip.close();
			resetAudioStream(clip);
			currentFrame = 0L;
			clip.setMicrosecondPosition(0);
			this.play(clip);
			break;
		case 4:
			currentFrame = 0L;
			clip.stop();
			clip.close();
			System.out.println("Stopped !");
			break;
		default: System.out.println("Invalid option selected");
		}
	}
	// Reset the audio stream
	private void resetAudioStream(Clip clip) throws Exception {
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
		clip.open(inputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);		
	}

	public String getSongId() {
		return songId;
	}
	public void setSongId(String songId) {
		this.songId = songId;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Song [songId=" + songId + ", songName=" + songName + ", artist=" + artist + ", genre=" + genre
				+ ", album=" + album + ", duration=" + duration + "]";
	}
}
