package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import baseball.Player;
import baseball.PlayerCollection;
import baseball.Position;
import baseball.Draft;
import baseball.FantasyDraft;

public class DraftTest {
	public String FILENAME;
	
	public Draft currentDraft;
	public Draft differentDraft;
	public Player pitcher;
	public Player notPitcher;
	public Player notPitcher2;
	@Before
	public void setUp() throws Exception {
		FILENAME = "unitTest";
		currentDraft = new Draft();

		pitcher = new Player("Bob", "Ross", Position.PITCHER, "DET");
		notPitcher = new Player("Mr.", "Rogers", Position.FIRSTBASEMAN, "CHC");
		notPitcher2 = new Player("Luke", "Rogers", Position.FIRSTBASEMAN, "CHC");

		currentDraft.AvailablePlayers.Players.add(pitcher);
		currentDraft.AvailablePlayers.Players.add(notPitcher);
		currentDraft.AvailablePlayers.Players.add(notPitcher2);
		
		differentDraft = new Draft();

		differentDraft.AvailablePlayers.Players.add(pitcher);
		differentDraft.AvailablePlayers.Players.add(notPitcher);
		differentDraft.AvailablePlayers.Players.add(notPitcher2);
	}

	@After
	public void teardown() throws Exception{
		File file = new File(FILENAME + ".fantasy.txt");
		file.delete();
	}
	
	@Test
	public void testDraftConstructor() {
		var draft = new Draft();
		assertNotNull(draft.AvailablePlayers);
		assertNotNull(draft.UnavailablePlayers);
		assertEquals(4, draft.LeagueMembers.size());
	}

	@Test
	public void testDraftPlayer() {
		currentDraft.DraftPlayer("A", "Ross");
		assertTrue(currentDraft.LeagueMembers.get("A").Players.contains(pitcher));
		currentDraft.DraftPlayer("A", "Toss");
		assertTrue(currentDraft.LeagueMembers.get("A").Players.size() == 1);
		currentDraft.DraftPlayer("A", "Ross");
		assertTrue(currentDraft.LeagueMembers.get("A").Players.size() == 1);
		currentDraft.DraftPlayer("B","Rogers");
		assertEquals(0,currentDraft.LeagueMembers.get("B").Players.size());
		currentDraft.DraftPlayer("B","Rogers, M");
		assertEquals(1,currentDraft.LeagueMembers.get("B").Players.size());
		currentDraft.DraftPlayer("B","Rogers, L");
		assertEquals(1,currentDraft.LeagueMembers.get("B").Players.size());
	
	}
	

	@Test
	public void testSave() {
		currentDraft.DraftPlayer("B", "Ross");
		assertTrue(currentDraft.save(FILENAME));
		assertFalse(currentDraft.save("?"));
	}
	
	@Test
	public void testEquals() {
		assertTrue(currentDraft.equals(currentDraft));
		assertFalse(currentDraft.equals(new Object()));
		assertTrue(currentDraft.equals(differentDraft));
	
		differentDraft.UnavailablePlayers.Players.add(pitcher);
		assertFalse(currentDraft.equals(differentDraft));
		differentDraft.AvailablePlayers.Players.remove(0);
		assertFalse(currentDraft.equals(differentDraft));

		differentDraft.AvailablePlayers.Players.remove(0);
		assertFalse(currentDraft.equals(differentDraft));

		differentDraft.LeagueMembers.remove("A");
		assertFalse(currentDraft.equals(differentDraft));

		differentDraft.AllPlayers.Players.add(notPitcher);
		assertFalse(currentDraft.equals(differentDraft));

		differentDraft.DraftCounter = 5;
		assertFalse(currentDraft.equals(differentDraft));

		differentDraft.lm = '~';
		assertFalse(currentDraft.equals(differentDraft));
	}
	
	@Test
	public void testRestore() {
		currentDraft.save(FILENAME);
		var restoredDraft = currentDraft.restore(FILENAME);
		assertTrue(currentDraft.equals(currentDraft));
		assertFalse(currentDraft.equals(new Object()));
		assertTrue(currentDraft.equals(restoredDraft));
	}
	
	@Test
	public void testRestoreFail() {
		assertNull(currentDraft.restore("?"));
	}

	
	@Test
	public void PrintRankingsTest() {
		Draft testDraft = new Draft();

		Player test1 = new Player("Test", "Name", Position.CATCHER, "LOL", 1, 1.5f,
				2.5f, 3.5f, 5);
		
		testDraft.DraftPlayer("A", test1.FullName());
		testDraft.PrintRankings();
	}
	
	@Test
	public void PrintRankingsArgumentTest() {
		Draft testDraft = new Draft();
		Player test1 = new Player("Test", "Name", Position.CATCHER, "LOL", 1, 1.5f,
				2.5f, 3.5f, 5);
		
		testDraft.DraftPlayer("A", test1.FullName());
		
		testDraft.PrintRankings(Position.CATCHER);
	}
	
	@Test
	public void PrintPitcherRankingsTest() {
		Draft testDraft = new Draft();
		Player test1 = new Player("Test", "Name", Position.PITCHER, "LOL", 1, 1.5f,
				2, 3.5f, 5);
		
		testDraft.DraftPlayer("A", test1.FullName());
		
		testDraft.PrintPitcherRankings();
	}
	
	@Test
	public void SetEvalFunTest() {
		String test = "AVG + 4";
		String test1 = "OBP + 10";
		String test2 = "AB + 3";
		String test3 = "SLG + 8";
		String test4 = "SB + 7";
		
		String testminus = "AVG - 5";
		String testminus1 = "OBP - 3";
		String testminus2 = "AB - 5";
		String testminus3 = "SLG - 2";
		String testminus4 = "SB - 9";
				
		Draft testDraft = new Draft();
		
		testDraft.SetEvalFunExpression(test);
		testDraft.SetEvalFunExpression(test1);
		testDraft.SetEvalFunExpression(test2);
		testDraft.SetEvalFunExpression(test3);
		testDraft.SetEvalFunExpression(test4);
		
		testDraft.SetEvalFunExpression(testminus);
		testDraft.SetEvalFunExpression(testminus1);
		testDraft.SetEvalFunExpression(testminus2);
		testDraft.SetEvalFunExpression(testminus3);
		testDraft.SetEvalFunExpression(testminus4);

	}
	
	@Test
	public void SetPEvalFunTest() {
		String test = "G + 10";
		String test1 = "GS + 3";
		String test2 = "ERA + 7";
		String test3 = "IP + 4";
		String test4 = "BB + 2";
		
		String testminus = "G - 10";
		String testminus1 = "GS - 3";
		String testminus2 = "ERA - 7";
		String testminus3 = "IP - 4";
		String testminus4 = "BB - 2";
		
		Draft testDraft = new Draft();
		
		testDraft.SetPEvalFunExpression(test);
		testDraft.SetPEvalFunExpression(test1);
		testDraft.SetPEvalFunExpression(test2);
		testDraft.SetPEvalFunExpression(test3);
		testDraft.SetPEvalFunExpression(test4);
		
		testDraft.SetPEvalFunExpression(testminus);
		testDraft.SetPEvalFunExpression(testminus1);
		testDraft.SetPEvalFunExpression(testminus2);
		testDraft.SetPEvalFunExpression(testminus3);
		testDraft.SetPEvalFunExpression(testminus4);
		
		
	}
}
