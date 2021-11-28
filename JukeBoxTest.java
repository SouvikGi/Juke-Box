package jukeBox;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.InputMismatchException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JukeBoxTest {
	PlayList playlist;
	Podcast podcast;
	Song song;
	User user;
	SongList songlist;
	Statement smt;
	Connection conn;
	
	@BeforeEach
	void setup() {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","Souvik#1234");
		smt = conn.createStatement();
		
		}catch(SQLException e) {
			System.out.println("Exception Arised : "+e);
		}
		catch(ClassNotFoundException e) {
			System.out.println("Exception Arised : "+e);
		}
	}
	
	@AfterEach
    void tearDown() {
		conn = null;
		smt = null;
	}
	

	@Test
	void testJukeBoxMain() {
		try {
			assertEquals("Invalid Choice !", JukeBox.playlistOption("souvik#3", smt, 5));
			assertEquals("Invalid Choice !", JukeBox.podcastOption("souvik#3", smt, 5));
			assertEquals(false, JukeBox.option("souvik#3", 6, smt, false));
			assertEquals(true, JukeBox.option("souvik#3", 4, smt, false));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testUser() {
		try {
			assertEquals(false, User.option("souvik#3", "Souvik Ghosh", "sg#123", smt, 1, false));
			assertEquals(true, User.option("souvik#3", "Souvik Ghosh", "sg#123", smt, 2, false));
			assertEquals(false, User.option("souvik#3", "Souvik Ghosh", "sg#123", smt, 4, false));
		} catch (SQLException | AccountExistsException e) {
			e.printStackTrace();
		}
	}

}
