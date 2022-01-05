package baseball;

import net.objecthunter.exp4j.ExpressionBuilder;

public class Player {
	public String FirstName;
	public String LastName;

	public Position Position;
	public HittingStats HitterStats;
	public PitchingStats PitcherStats;
	public int DraftingOrder;
	public String RealTeam;
	
	public Player() {
		HitterStats = new HittingStats();
		PitcherStats = new PitchingStats();
	}

	/**
	 * Create a new player and sets up their data quickly
	 * 
	 * @param firstName
	 * @param lastName
	 * @param position
	 * @param realTeam
	 */
	public Player(String firstName, String lastName, Position position, String realTeam) {
		FirstName = firstName;
		LastName = lastName;
		Position = position;
		RealTeam = realTeam;
		HitterStats = new HittingStats();
		PitcherStats = new PitchingStats();
	}

	public Player(String firstName, String lastName, Position position, String realTeam, int atBat,
			float battingAverage, float slugPercent, float onBasePercent, int totalStolenBases) {
		FirstName = firstName;
		LastName = lastName;
		Position = position;
		RealTeam = realTeam;
		
		HitterStats = new HittingStats(atBat, battingAverage, slugPercent, onBasePercent, totalStolenBases);

	}

	public Player(String firstName, String lastName, Position position, String realTeam, int gamesPlayed,
			float inningsPitched, int walksAllowed, float earnedRunAverage, int gamesStarted) {
		FirstName = firstName;
		LastName = lastName;
		Position = position;
		RealTeam = realTeam;
		
		PitcherStats = new PitchingStats(gamesStarted, earnedRunAverage, inningsPitched, walksAllowed);
	}

	public String FirstInitial() {
		return FirstName.substring(0, 1);
	}

	public String FullName() {
		return FirstName + " " + LastName;
	};

	public String LastNameFirstInitial() {
		return LastName + ", " + FirstInitial();
	}
	
	public int GetDraftingOrder() {
		return DraftingOrder;
	}

	public double EvalFunctionResult() {
		if (this.Position == baseball.Position.PITCHER) {
			if (FantasyDraft.CurrentDraft.PEvalFunExpression == null) {
				return this.PitcherStats.InningsPitched;
			}else {
				try {
					double result = new ExpressionBuilder(FantasyDraft.CurrentDraft.PEvalFunExpression)
						.variables("G", "GS", "ERA", "IP", "BB")
						.build()
						.setVariable("G", (double)this.PitcherStats.GamesPlayed)
						.setVariable("GS", (double)this.PitcherStats.GamesStarted)
						.setVariable("ERA", (double)this.PitcherStats.EarnedRunAverage)
						.setVariable("IP", (double)this.PitcherStats.InningsPitched)
						.setVariable("BB", (double)this.PitcherStats.WalksAllowed)
						.evaluate();
					return result;
				} catch (Exception e) {
					System.out.println("An error occured with the PEVALFUN expression. Default is still being used. Please redefine and try again.");
					return this.PitcherStats.InningsPitched;
				}
			}
		} else {

			if (FantasyDraft.CurrentDraft.EvalFunExpression == null) {
				return this.HitterStats.BattingAverage;
			} else {
				try {
				double result = new ExpressionBuilder(FantasyDraft.CurrentDraft.EvalFunExpression)
						.variables("AVG","OBP","AB","SLG","SB")
						.build()
						.setVariable("AVG", (double)this.HitterStats.BattingAverage)
						.setVariable("OBP", (double)this.HitterStats.OnBasePercentage)
						.setVariable("AB", (double)this.HitterStats.AtBats)
						.setVariable("SLG",  (double)this.HitterStats.SluggingPercentage)
						.setVariable("SB", (double)this.HitterStats.StolenBases)
						.evaluate();
					return result;
				} catch (Exception e) {
					System.out.println("An error occured with the EVALFUN expression. Default is still being used. Please redefine and try again.");
					return this.HitterStats.BattingAverage;
				}
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o.getClass() != Player.class) {
			return false;
		} else {
			var that = (Player) o;
			return this.FirstName.toLowerCase().equals(that.FirstName.toLowerCase())
					&& this.LastName.toLowerCase().equals(that.LastName.toLowerCase())
					&& this.DraftingOrder == that.DraftingOrder && this.Position.equals(that.Position)
					&& this.RealTeam.toLowerCase().equals(that.RealTeam.toLowerCase());
		}
	}
}
