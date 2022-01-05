package tests;
import static org.junit.Assert.*;

import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;

import baseball.Draft;
import baseball.FantasyDraft;
import baseball.Player;
import baseball.Position;

public class PlayerTests {
	public Player Pitcher;
	public Player NotPitcher;
	
	@Before
	public void setUp() throws Exception {
		FantasyDraft.CurrentDraft = new Draft();
		Pitcher = new Player("Bob","Ross",Position.PITCHER,"DET",30,43.3f,3,.23f,3);
		NotPitcher = new Player("Mr.","Rogers",Position.FIRSTBASEMAN,"CHC",4,0.45f,0.6f,0.32f,3);
	}

	@Test
	public void FullNameTest() {
		assertEquals("Bob Ross", Pitcher.FullName());
		assertEquals("Mr. Rogers", NotPitcher.FullName());
	}
	
	@Test
	public void FirstInitialTest() {
		assertEquals("B",Pitcher.FirstInitial());
		assertEquals("M", NotPitcher.FirstInitial());
	}
	
	@Test
	public void LastNameFirstInitialTest() {
		assertEquals("Ross, B", Pitcher.LastNameFirstInitial());
	}
	
	@Test
	public void GetDraftingOrderTest() {
		assertEquals(Pitcher.DraftingOrder, Pitcher.GetDraftingOrder());
	}
	
	@Test
	public void EvalFunctionResultTest_Pitcher() {
		assertEquals((double)Pitcher.PitcherStats.InningsPitched, Pitcher.EvalFunctionResult(),0.0f);
		FantasyDraft.CurrentDraft.PEvalFunExpression = "IP + IP";
		assertEquals((double)Pitcher.PitcherStats.InningsPitched + Pitcher.PitcherStats.InningsPitched, Pitcher.EvalFunctionResult(),0.0f);
		FantasyDraft.CurrentDraft.PEvalFunExpression = "BREAK";
		assertEquals((double)Pitcher.PitcherStats.InningsPitched, Pitcher.EvalFunctionResult(),0.0f);
	}
	
	@Test
	public void EvalFunctionResultTest_NotPitcher() {
		assertEquals((double)NotPitcher.HitterStats.BattingAverage, NotPitcher.EvalFunctionResult(),0.0f);
		FantasyDraft.CurrentDraft.EvalFunExpression = "AVG + 1";
		assertEquals((double)NotPitcher.HitterStats.BattingAverage + 1, NotPitcher.EvalFunctionResult(),0.0f);
		FantasyDraft.CurrentDraft.EvalFunExpression = "BREAK";
		assertEquals((double)NotPitcher.HitterStats.BattingAverage, NotPitcher.EvalFunctionResult(),0.0f);
	}
}
