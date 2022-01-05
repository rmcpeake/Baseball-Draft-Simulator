package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import baseball.Draft;
import baseball.FantasyDraft;
import baseball.Player;
import baseball.PlayerCollection;
import baseball.Position;

public class PlayerCollectionTests {
	public PlayerCollection availablePlayers;
	public PlayerCollection test;
	public Draft CurrentDraft;
	
	@Before
	public void setUp() throws Exception {
		availablePlayers = new PlayerCollection();
		
		var pitcher = new Player("Bob","Ross",Position.PITCHER,"DET");
		var notPitcher = new Player("Mr.","Rogers",Position.FIRSTBASEMAN,"CHC");
		
		availablePlayers.Players.add(pitcher);
		availablePlayers.Players.add(notPitcher);
		
	}

	@Test
	public void GetPlayerByNameTest() {
		assertEquals(availablePlayers.Players.get(0), availablePlayers.GetPlayerByName("Ross"));
		assertEquals(availablePlayers.Players.get(0), availablePlayers.GetPlayerByName("Ross, B"));
		assertEquals(availablePlayers.Players.get(0), availablePlayers.GetPlayerByName("Bob Ross"));
		
		assertNull(availablePlayers.GetPlayerByName("Crying"));

	}
	
	@Test
	public void GetPlayersByPositionTest() {
		assertEquals(1,availablePlayers.GetPlayersByPosition(Position.PITCHER).size());
	}
	
	@Test
	public void GetPlayersByNameTest() {
		assertEquals(1, availablePlayers.GetPlayersByName("Ross").size());
		assertEquals(1, availablePlayers.GetPlayersByName("Bob Ross").size());
		assertEquals(1, availablePlayers.GetPlayersByName("Ross, B").size());
	}
	
	@Test
	public void CanDraftTest() {
		assertTrue(availablePlayers.CanDraft(Position.PITCHER));
		assertFalse(availablePlayers.CanDraft(Position.FIRSTBASEMAN));
		var pitcher = new Player("Bob","Ross",Position.PITCHER,"DET");
		availablePlayers.Players.add(pitcher);
		availablePlayers.Players.add(pitcher);
		availablePlayers.Players.add(pitcher);
		availablePlayers.Players.add(pitcher);
		assertFalse(availablePlayers.CanDraft(Position.PITCHER));
		assertTrue(availablePlayers.CanDraft(Position.CATCHER));
	}
	
	@Test
	public void PositionsAvailableTest() {
		assertTrue(availablePlayers.PositionsAvailable().contains(Position.CATCHER));
	}
	
	@Test
	public void EqualsTest() {
		assertTrue(availablePlayers.equals(availablePlayers));
		var differentPlayers = new PlayerCollection();
		assertFalse(availablePlayers.equals(differentPlayers));
		assertFalse(availablePlayers.equals(new Object()));
	}

	@Before
	public void setUp2() throws Exception {
		CurrentDraft = new Draft();
		var c = new Player("catcher", "catch", Position.CATCHER, "DET");
		var fb = new Player("first", "1base", Position.FIRSTBASEMAN, "DET");
		var sb = new Player("second", "2base", Position.SECONDBASEMAN, "DET");
		var tb = new Player("third", "3base", Position.THIRDBASEMAN, "DET");
		var ss = new Player("short", "stop", Position.SHORTSTOP, "DET");
		var lf = new Player("left", "fieldl", Position.LEFTFIELDER, "DET");
		var cf = new Player("center", "fieldc", Position.CENTERFIELDER, "DET");
		var rf = new Player("right", "fieldr", Position.RIGHTFIELDER, "DET");
		var p = new Player("pitcher", "pitch", Position.PITCHER, "DET");
		CurrentDraft.AvailablePlayers.Players.add(c);
		CurrentDraft.AvailablePlayers.Players.add(fb);
		CurrentDraft.AvailablePlayers.Players.add(sb);
		CurrentDraft.AvailablePlayers.Players.add(tb);
		CurrentDraft.AvailablePlayers.Players.add(ss);
		CurrentDraft.AvailablePlayers.Players.add(lf);
		CurrentDraft.AvailablePlayers.Players.add(cf);
		CurrentDraft.AvailablePlayers.Players.add(rf);
		CurrentDraft.AvailablePlayers.Players.add(p);
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	}
	
	@Test
	public void PrintStarsTest() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
		
		CurrentDraft.DraftPlayer("A", "catch");
		CurrentDraft.DraftPlayer("A", "1base");
		CurrentDraft.DraftPlayer("A", "2base");
		CurrentDraft.DraftPlayer("A", "3base");
		CurrentDraft.DraftPlayer("A", "stop");
		CurrentDraft.DraftPlayer("A", "fieldl");
		CurrentDraft.DraftPlayer("A", "fieldc");
		CurrentDraft.DraftPlayer("A", "fieldr");
		CurrentDraft.DraftPlayer("A", "pitch");
		outContent.reset();
		CurrentDraft.getPC("A").PrintStars();
		
		String expected = "Roster Printed out in draft order \n"+
				"\r\n"+
				"1. CATCHER catcher catch\r\n"+
				"2. FIRSTBASEMAN first 1base\r\n"+
				"3. SECONDBASEMAN second 2base\r\n"+
				"4. THIRDBASEMAN third 3base\r\n"+
				"5. SHORTSTOP short stop\r\n"+
				"6. LEFTFIELDER left fieldl\r\n"+
				"7. CENTERFIELDER center fieldc\r\n"+
				"8. RIGHTFIELDER right fieldr\r\n"+
				"9. PITCHER pitcher pitch\r\n"+
				"\r\n";
		assertEquals(expected, outContent.toString());
		
	}
	
	@Test
	public void PrintRosterTest() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
		CurrentDraft.DraftPlayer("A", "catch");
		CurrentDraft.DraftPlayer("A", "1base");
		CurrentDraft.DraftPlayer("A", "2base");
		CurrentDraft.DraftPlayer("A", "3base");
		CurrentDraft.DraftPlayer("A", "stop");
		CurrentDraft.DraftPlayer("A", "fieldl");
		CurrentDraft.DraftPlayer("A", "fieldc");
		CurrentDraft.DraftPlayer("A", "fieldr");
		CurrentDraft.DraftPlayer("A", "pitch");
		outContent.reset();
		CurrentDraft.getPC("A").PrintRoster();
		
		String expected = "Roster printed out in order by position: \n"+
				"\r\n"+
				"CATCHER catcher catch\r\n"+
				"FIRSTBASEMAN first 1base\r\n"+
				"SECONDBASEMAN second 2base\r\n"+
				"THIRDBASEMAN third 3base\r\n"+
				"SHORTSTOP short stop\r\n"+
				"LEFTFIELDER left fieldl\r\n"+
				"CENTERFIELDER center fieldc\r\n"+
				"RIGHTFIELDER right fieldr\r\n"+
				"PITCHER pitcher pitch\r\n"+
				"-\r\n"+
				"-\r\n"+
				"-";
				
		assertEquals(expected, outContent.toString().trim());
	}

}
