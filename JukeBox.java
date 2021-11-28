package jukeBox;
import java.sql.*;
import java.util.*;

public class JukeBox {

	static User user = new User();
	static Song song = new Song();
	static PlayList playlist = new PlayList();
	static Podcast podcast = new Podcast();
	static SongList songlist = new SongList();
	static int opt;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","Souvik#1234");
			Statement smt = conn.createStatement();
			jukeBoxSystem(smt);							//calling the main interface
		}
		catch (Exception e) {
			System.out.println("Exception Arised : "+e);
		}

	}
	// main interface for the program
	private static void jukeBoxSystem(Statement smt) throws Exception {
		try {
			String userId = user.userInterface(smt);//called from user for validation or ceate new account
			if(userId.equals("exit")) {
				outDisplay();
				return;
			}
			boolean done = false;
			while(!done) {
				System.out.println("\n 1.Song  2.Playlist  3.Podcast  4.exit");
				opt = sc.nextInt();
				done=option(userId,opt,smt,done);
			}
		}catch(InputMismatchException e) {
			System.out.println("Please enter a number !\n");
		}
	}
	// options for the main interface
	public static boolean option(String userId, int opt, Statement smt, boolean done) throws Exception {
		switch (opt) {
		case 1: {
			song.playSong(userId,smt,"song");			//calling method from song for sub options
			done=false;
			break;
		}
		case 2: {
			System.out.println("1.Create Playlist  2.Existing Playlist  3.Play Playlist");
			int choice = sc.nextInt();
			playlistOption(userId,smt, choice);			//sub options for playlist
			done=false;									//calling method from playlist
			break;
		}
		case 3: {
			System.out.println("1.Display Podcast  2.Create Podcast");
			int choice = sc.nextInt();
			podcastOption(userId,smt,choice);			//sub options for podcast
			done=false;						 			//calling method from podcast
			break;
		}
		case 4: {
			outDisplay();
			done = true;
			break;
		}
		default:
			System.out.println("Invalid option selected !");
		}
		return done;		
	}
	public static String playlistOption(String userId, Statement smt, int choice) throws Exception {
		if(choice == 1)	{
			playlist.doPlaylist(userId,smt,"create");   // calling method from playlist to create
			return "Create";
		}
		else if(choice == 2) {
			playlist.doPlaylist(userId,smt,"existing"); // calling method from playlist to update existing playlist
			return "Existing";
		}
		else if(choice == 3) {
			playlist.playSong(userId,smt,"playlist");   // calling method from playlist to play
			return "Play";
		}
		System.out.println("Invalid Choice !");
		return "Invalid Choice !";	
	}

	public static String podcastOption(String userId, Statement smt, int choice) throws Exception {
		if(choice == 1)	{
			Podcast.displayPodcast(smt);				// calling method from podcast to display
			return 	"Display";
		}
		else if(choice == 2) {
			podcast.createPodcast(userId, smt);			// calling method from podcast to create
			return 	"Create";
		}
		System.out.println("Invalid Choice !");
		return 	"Invalid Choice !";
	}
	
	public static void outDisplay() {
		System.out.println("|**************************************************|\n"
				+ "|    *    Thank You For Using This Service    *    |\n"
				+ "****************************************************");		
	}
}

class InvalidChoiceException  extends Exception  {
	public InvalidChoiceException (String str)  
	{  
		super(str);
	}  
}

class AccountExistsException  extends Exception  {
	public AccountExistsException (String str)  
	{  
		super(str);
	}  
}
