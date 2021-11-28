package jukeBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Podcast extends SongList{
	static Scanner sc = new Scanner(System.in);

	private String podcastId; //PCID001
	private String podcastName;
	private String celebrityName;
	private Date datePublished;

	public Podcast() {}
	
	public Podcast(String podcastId, String podcastName, String celebrityName, Date datePublished) {
		super();
		this.podcastId = podcastId;
		this.podcastName = podcastName;
		this.celebrityName = celebrityName;
		this.datePublished = datePublished;
	}
	//method to create podcast
	public void createPodcast(String userId,Statement smt) throws Exception {
		char opt;
		boolean done = false;
		podcastId = generatePodcastId(userId,smt);							//calling to get podcastId
		while(!done) {
			searchSong(smt);												//calling from song to search songs
			System.out.println("\nEnter song index to add or go back(0)");
			int i = sc.nextInt();
			if(i==0 || i>newList.size()) return;
			super.setDetails(newList.get(i-1).getSongId(), null, smt, podcastId); //calling from songlist to update details in database
			System.out.println("Song Added !");
			System.out.println("Do you wish to add another song (Y/N) ?");
			opt = sc.next().charAt(0);
			if(opt=='Y' || opt=='y') {
				newList.clear();
				done = false;
			}
			else {
				done = true;
			}
		}
		System.out.println("podcast Created Successfully !");
	}
	//take details from user to create podcast
	private String generatePodcastId(String userId, Statement smt) throws SQLException {
		boolean done = false;
		while(!done) {
			System.out.println("Enter name of the Podcast :");
			podcastName = new Scanner(System.in).nextLine();
			System.out.println("Enter Celebrity Name :");
			celebrityName = new Scanner(System.in).nextLine();
			ResultSet check = smt.executeQuery("select * from podcast where podcastName ='"+podcastId+"' and celebrityName ='"+celebrityName+"'");
			if(check.next()) {
				System.out.println("Podcast already available with same name of "+celebrityName+" !");
			}
			else {
				System.out.println("Enter date Published (YYYY-MM-DD) :");
				datePublished = Date.valueOf(sc.next());
				ResultSet rs = smt.executeQuery("select podcastId from podcast");
				if(!rs.next()) {
					podcastId = "PCID101";
					done = true;
				}
				else {
					ResultSet rs1 = smt.executeQuery("select max(podcastId) from podcast");
					while(rs1.next()) {
						podcastId = "PCID" + (Integer.parseInt(rs1.getString(1).substring(4, rs1.getString(1).length()))+1);
					}
					done = true;
				}
				smt.executeUpdate("insert into podcast values('"+podcastId+"','"+podcastName+"','"+celebrityName+"','"+datePublished+"')");
			}
		}
		return podcastId;
	}
	//method to display podcasts available
	public static List<Podcast> displayPodcast(Statement smt) throws SQLException {
		List<Podcast> podList = new ArrayList<Podcast>();
		podList.clear();
		ResultSet rs = smt.executeQuery("select * from podcast");
		if(!rs.next()) {
			System.out.println("No Podcast Available !");
			return podList;
		}
		ResultSet rs1 = smt.executeQuery("select * from podcast");
		while(rs1.next()) {
			Podcast podcast = new Podcast(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getDate(4));
			podList.add(podcast);											//adding all the podcast to a list
		}
		System.out.format("%3s %30s %25s %12s","SL#","Podcast","Celebrity","Published");
		System.out.print("\n/-------------------------------------------------------------------------/");
		podList.stream().forEach((o)->{			
			System.out.format("\n%3s %30s %25s %12s",podList.indexOf(o)+1,o.podcastName,o.celebrityName,o.datePublished.toString());
		});
		System.out.println("\n/-------------------------------------------------------------------------/");
		return podList;
	}
	
	public String getPodcastId() {
		return podcastId;
	}
	public void setPodcastId(String podcastId) {
		this.podcastId = podcastId;
	}
	public String getCelebrityName() {
		return celebrityName;
	}
	public void setCelebrityName(String celebrityName) {
		this.celebrityName = celebrityName;
	}
	public String getPodcastName() {
		return podcastName;
	}
	public void setPodcastName(String podcastName) {
		this.podcastName = podcastName;
	}
	public Date getDatePublished() {
		return datePublished;
	}
	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}
	
	@Override
	public String toString() {
		return "Podcast [PodcastId=" + podcastId + ", PodcastName=" + podcastName + ", CelebrityName=" + celebrityName + ", DatePublished=" + datePublished + "]";
	}
}
