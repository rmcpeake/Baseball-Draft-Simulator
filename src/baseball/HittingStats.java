package baseball;

public class HittingStats extends Stats  {
	public float BattingAverage;
	public float OnBasePercentage;
	public int AtBats;
	public float SluggingPercentage;
	public int StolenBases;
	
	public HittingStats() {
	}
	
	public HittingStats(int atBat, float battingAverage, float slugPercent, float onBasePercent, int totalStolenBases) {
		BattingAverage = battingAverage;
		OnBasePercentage = onBasePercent;
		AtBats = atBat;
		SluggingPercentage = slugPercent;
		StolenBases = totalStolenBases;
		
	}
}
