package baseball;

public class PitchingStats extends Stats {
	public int GamesStarted;
	public float EarnedRunAverage;
	public float InningsPitched;
	public int WalksAllowed;
	
	public PitchingStats() {
		
	}
	
	public PitchingStats(int gamesStarted, float earnedRunAverage, float inningsPitched, int walksAllowed) {
		GamesStarted = gamesStarted;
		EarnedRunAverage = earnedRunAverage;
		InningsPitched = inningsPitched;
		WalksAllowed = walksAllowed;
	}
}
