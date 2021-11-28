package jukeBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayList extends SongList {
	
	Podcast podcast = new Podcast();
	static Scanner sc = new Scanner(System.in);
	static List<Podcast> podList = new ArrayList<Podcast>();
	static List<String> playlist = new ArrayList<String>();
	
	private String playlistId; //PID1001
	private String playlistName;
	private String userId;
	//method to create or update playlist
	public String doPlaylist(String userId, Statement smt, String choice) throws Exception {
		int opt;
		boolean done = false;
		if(choice.equals("create")) playlistId = create(userId,smt);
		if(choice.equals("existing")) {
			newList.clear();
			playlist = displayPlaylist(userId,smt);					//calling to display available playlists
			if(playlist.isEmpty()) {
				System.out.println("Playlist not found !");
				return "Playlist not found !";
			}
			System.out.println("Select Playlist :");
			playlistId = playlist.get(sc.nextInt()-1);				//getting playlistId to update playlist
		}
		while(!done) {
			System.out.println("1.Add Song  2.Add Podcast  3.Exit");
			opt = sc.nextInt();
			switch (opt) {
			case 1 : {
				addSong(userId,smt);								//calling to add songs to playlist
				break;
			}
			case 2 : {
				addPodcast(userId,smt);								//calling to add podcast to playlist
				break;
			}
			case 3 : {
				done = true;
				break;
			}
			default:
				System.out.println("Invalid option selected !");
			}
		}
		if(choice.equals("existing")) {
			System.out.println("Playlist Updated Successfully !");
			return "Playlist Updated Successfully !";
		}
		System.out.println("Playlist Created Successfully !");
		return "Playlist Created Successfully !";
	}
	//method to display available playlist
	public static List<String> displayPlaylist(String userId, Statement smt) throws SQLException {
		int in =1;
		List<String> list = new ArrayList<String>();
		ResultSet rs1 = smt.executeQuery("select * from playlist where userid = '"+userId+"'");
		while(rs1.next()) {
			System.out.println(in+"\t"+rs1.getString(2));				//displaying the playlists
			list.add(rs1.getString(1));									//adding playlistId to a list
			in++;
		}
		return list;
	}
	//method to take user input and to create playlist and return playlistId
	private String create(String userId, Statement smt) throws SQLException {
		boolean done = false;
		while(!done) {
			System.out.println("Enter playlist Name :");
			playlistName = new Scanner(System.in).nextLine();
			ResultSet check = smt.executeQuery("select * from playlist where userid ='"+userId+"' and playlistname ='"+playlistName+"'");
			if(check.next()) {
				System.out.println("Playlist already available with same name !");
			}
			else {
				ResultSet rs1 = smt.executeQuery("select playlistId from playlist");
				if(!rs1.next()) {
					playlistId = "PID1001";
					done = true;
				}
				else {
					ResultSet rs = smt.executeQuery("select max(playlistid) from playlist");
					while(rs.next()) {
						playlistId = "PID" + (Integer.parseInt(rs.getString(1).substring(3, rs.getString(1).length()))+1);//auto generated playlistId
					}
					done = true;
				}
				smt.executeUpdate("insert into playlist values('"+playlistId+"','"+playlistName+"','"+userId+"')");
			}
		}
		return playlistId;
	}
	//method to add songs to a playlist
	public String addSong(String userId, Statement smt) throws Exception {
		char opt;
		boolean done = false;
		while(!done){
			super.searchSong(smt);
			System.out.println("\nEnter song index to add or go back(0)");
			int i = sc.nextInt();
			if(i==0 || i>newList.size()) return "Song Added !";
			super.setSongId(newList.get(i-1).getSongId());
			super.setDetails(super.getSongId(), playlistId, smt, null);	//calling from songlist to update the songs added to playlist
			System.out.println("Song Added !");
			System.out.println("Do you wish to add another song (Y/N) ?");
			opt = sc.next().charAt(0);
			if(opt!='Y' || opt!='y') {
				done = true;
			}
			else {
				newList.clear();
				done = false;
			}
		}
		return "Song Added !";
	}
	//method to add podcast to a playlist
	public String addPodcast(String userId, Statement smt) throws SQLException {
		boolean done = false;
		podList.clear();
		podList = Podcast.displayPodcast(smt);						//calling from podcast to display available podcasts
		while(!done) {
			System.out.println("\nEnter Podcast index to add or go back(0)");
			int i = sc.nextInt();
			if(i==0 || i>podList.size()) return "Podcast Added !";
			podcast.setPodcastId(podList.get(i-1).getPodcastId());	//updating podcastId in podcast
			String result = super.checkAndSet(podcast.getPodcastId(),playlistId,smt);	//calling from songlist for validating & updating podcast
			System.out.println(result);
			if(result.equals("Podcast Added !")) done = true;
		}
		return "Podcast added !";
	}

	//method to display and play the songs in playlist
	public String play(String userId, Statement smt) throws SQLException {
		newList.clear();
		List<String> list = super.getSongIdList(userId, smt);		//calling from songlist to get the list of SongId of the playlist
		if(list.isEmpty()) return "Empty Playlist !";
		list.stream().forEach((o)->{
			try {
				ResultSet rs = smt.executeQuery("select * from song where songid = '"+o+"'");
				while(rs.next()) {
					Song song = new Song(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
					newList.add(song);								//adding songs need to display
				}
			} catch (SQLException e) {
				System.out.println("Error occured while retriving Data..!");
			}
		});
		if(!newList.isEmpty()) {
			super.display(newList);									//calling from song to display songs available in playlist
			return "NotEmpty";
		}
		else return "Empty Playlist !";		
	}
	
	public String getPlayListId() {
		return playlistId;
	}
	public void setPlayListId(String playListId) {
		playlistId = playListId;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "PlayList [PlayListId=" + playlistId + ", PlaylistName=" + playlistName + ", UserId=" + userId + "]";
	}
}
