package jukeBox;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SongList extends Song{
	static Scanner sc = new Scanner(System.in);

	private int id; //AUTO-GENERATED(SEQUENCE)
	private String songId;
	private String playlistId;
	private String podcastId;
	//method store the details in database
	public void setDetails(String songId,String playlistId,Statement smt, String podcastId) throws SQLException {
		if(playlistId == null) {
			smt.executeUpdate("insert into songlist(songid,podcastid) values('"+songId+"','"+podcastId+"')");		
		}
		else {
			smt.executeUpdate("insert into songlist(songid,playlistid) values('"+songId+"','"+playlistId+"')");		
		}
	}
	//method get songId from database
	public List<String> getSongIdList(String userId,Statement smt) throws SQLException {
		List <String> list = new ArrayList<String>();
		List <String> allsongidList = new ArrayList<String>();
		List <String> songidList = new ArrayList<String>();
		int in = 1;
		list.clear();
		allsongidList.clear();
		songidList.clear();
		list = PlayList.displayPlaylist(userId,smt);		//calling from playlist to display ang return list of playlistId
		if(list.isEmpty()) return list;
		System.out.println("Select Playlist :");
		in = sc.nextInt();		
		ResultSet rs2 = smt.executeQuery("select songid from songlist where playlistid = '"+list.get(in-1)+"'");
		while(rs2.next()) {
			allsongidList.add(rs2.getString(1));
		}
		songidList = allsongidList.stream().distinct().collect(Collectors.toList());

		return songidList;
	}
	//method to validate and update podcast in playlist
	public String checkAndSet(String podcastId, String playlistId, Statement smt) throws SQLException {
		List<String> podcastSong = new ArrayList<String>();
		podcastSong.clear();
		ResultSet rs = smt.executeQuery("select * from songlist where playlistId ='"+playlistId+"' and podcastId ='"+podcastId+"'");
		if(rs.next()) return "Podcast already added in this Playlist !";
		
		ResultSet rs1 = smt.executeQuery("select songid from songlist where podcastid ='"+podcastId+"'");
		while(rs1.next()) {
			podcastSong.add(rs1.getString(1));
		}
		podcastSong.stream().forEach((o)->{
			try {
				smt.executeUpdate("insert into songlist(songId,playlistId,podcastId) values('"+o+"','"+playlistId+"','"+podcastId+"')");
			} catch (SQLException e) {
				System.out.println("Error orrured while updating data !");
			}
		});		
		return "Podcast Added !";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSongId() {
		return songId;
	}
	public void setSongId(String songId) {
		this.songId = songId;
	}
	public String getPlaylistId() {
		return playlistId;
	}
	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}
	public String getPodcastId() {
		return podcastId;
	}
	public void setPodcastId(String podcastId) {
		this.podcastId = podcastId;
	}
	
	@Override
	public String toString() {
		return "SongList [Id=" + id + ", SongId=" + songId + ", PlaylistId=" + playlistId + ", PodcastId=" + podcastId
				+ "]";
	}


	
}
