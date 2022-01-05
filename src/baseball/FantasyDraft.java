package baseball;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FantasyDraft {
	public static Scanner input;
	public static Draft CurrentDraft;
	public static boolean shouldQuit;
	public static boolean saveQuit;
	
	public static void main(String[] args) {
		input = new Scanner(System.in);
		CurrentDraft = new Draft();
		initializeAvaliablePlayerList();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("=============== Fantasy Baseball Drafting ===============");
		System.out.println("=== Madeline Wakevainen, Robert McPeake, Keegan Dwyer ===");
		System.out.println("----------------+ Created for COSC 381 +-----------------");
		System.out.println("=========== Type HELP for a List of Commands! ===========");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		run();
	}

	// TODO implement run
	public static void run() {
		var memberName = "";
		var playerName = "";
		var playerPosition = "";
		Scanner input = new Scanner(System.in);
		while (!shouldQuit) {
			try {
				System.out.println("Please enter a command:");
				String enteredCommand = input.nextLine();
				String functionToRun = enteredCommand.split(" ")[0].toLowerCase();
				switch (functionToRun) {
				case "odraft":
					if (enteredCommand.split("\"").length != 3) {
						System.out.println("Incorrect command format. No player drafted.");
					} else {
						playerName = enteredCommand.split("\"")[1].trim();
						memberName = enteredCommand.split("\"")[2].trim().toUpperCase();
					}
					if (playerName.isBlank()) {
						System.out.println("No player name specified.");
					} else if (!memberName.matches("[A-D]")) {
						System.out.println("Invalid league member name.");
						System.out.println();
					} else {
						CurrentDraft.DraftPlayer(memberName, playerName);
					}
					break;
				case "idraft":
					if (enteredCommand.split("\"").length != 2) {
						System.out.println("Incorrect command format. No player drafted.");
						System.out.println();
					} else {
						playerName = enteredCommand.split("\"")[1].trim();

						if (playerName.isBlank()) {
							System.out.println("No player name specified.");
						} else {
							CurrentDraft.DraftPlayer("A", playerName);
						}
					}
					break;
				case "overall": // with position given
					if (enteredCommand.length() > 7) {
						playerPosition = enteredCommand.split(" ")[1].trim();
						CurrentDraft.PrintRankings(Position.Parse(playerPosition));
					} else { // without position given
						CurrentDraft.PrintRankings();

					}
					break;
				case "poverall":
					playerPosition = enteredCommand.split("\"")[0].trim();
					CurrentDraft.PrintPitcherRankings();
					break;
				case "team":
					String mem = enteredCommand.split(" ")[1].trim();
					CurrentDraft.getPC(mem.toUpperCase()).PrintRoster();
					break;
				case "stars":
					mem = enteredCommand.split(" ")[1].trim();
					CurrentDraft.getPC(mem.toUpperCase()).PrintStars();
					break;
				case "restore":
					var restoreFileName = "";
					try {
						restoreFileName = enteredCommand.split(" ")[1];
					}catch (Exception e) {
						System.out.println("What is the name of the file you want to restore from?");
						restoreFileName = input.nextLine();
					}
					restoreFileName = restoreFileName.replace(".fantasy", "").replace(".txt", "");
					Draft restoredDraft = CurrentDraft.restore(restoreFileName);
					CurrentDraft = restoredDraft != null ? restoredDraft : CurrentDraft;
					break;
				case "evalfun":
					var expression = enteredCommand.toLowerCase().replaceAll("evalfun", "").toUpperCase()
							.replaceAll(" ", "");
					CurrentDraft.SetEvalFunExpression(expression);
					break;
				case "pevalfun":
					var pExpression = enteredCommand.toLowerCase().replaceAll("pevalfun", "").toUpperCase()
							.replaceAll(" ", "");
					CurrentDraft.SetPEvalFunExpression(pExpression);
					break;
				case "quit":
					System.out.println("Would you like to save? Y/N");
					String saveAnswer = input.nextLine().toLowerCase();
					if (saveAnswer.equals("y")) {
						saveQuit = true;
					} else {
						quit();
						break;
					}
				case "save":
					var fileName = "";
					try {
						fileName = enteredCommand.split(" ")[1];
					}catch (Exception e) {
						System.out.println("What would you like to name the file?");
						fileName = input.nextLine();
					}
					CurrentDraft.save(fileName);
					if (saveQuit) {
						quit();
					}
					break;
				case "help":
					help();
					break;
				default:
					help();
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error: Command syntax incorrect.");

			}
		}
		input.close();
	}

	public static void quit() {
		System.out.println("Quitting the program");
		shouldQuit = true;
	}

	public static void help() {
		System.out.println("\nList of Commands:");
		System.out.println("=============================================");
		System.out.println(
				"ODRAFT [playerName] [leagueMember] :: Given the name and letter of the desired league, drafts player to league.");
		System.out.println("IDRAFT [playerName] :: Given the name of the desired player, drafts player to League A.");
		System.out.println("OVERALL [position] :: Prints the ranking of all non-pitcher players.");
		System.out.println("POVERALL :: Prints the ranking of all pitcher players.");
		System.out.println("TEAM [leagueMember] :: Prints the current roster of players in the given league.");
		System.out.println(
				"STARS [leagueMember] :: Prints current roster of players in the order they were drafted, in the given league.");
		System.out.println("SAVE [fileName] :: Saves the league to a file of the given name.");
		System.out.println("QUIT :: Terminates the program.");
		System.out.println("RESTORE [fileName] :: Restores the league from a file of the given name.");
		System.out.println("EVALFUN [expression] :: Evaluates the given expression on non-pitcher players.");
		System.out.println("PEVALFUN [expression] :: Evaluates the given expression on pitcher players.");
		System.out.println("HELP :: Displays all commmands and syntax.");
		System.out.println("=============================================\n");
	}

	public static void initializeAvaliablePlayerList() {
		String row;
		String fName;
		String lName;
		Position playerPos;
		String playerTeam;
		int playerAtBat;
		float battingAve;
		float slugPerc;
		float onBasePerc;
		int playerStolenBases;

		String pFName;
		String pLName;
		Position pPlayerPos;
		String pPlayerTeam;
		int pGamesPlayed;
		float pInningsPitched;
		int pWalksAllowed;
		float pEarnedRunAverage;
		int pGamesStarted;

		try { // Reads in from TSV file and allocated line data to player objects and adds
				// them to the Current Draft
			BufferedReader hitterReader = new BufferedReader(new FileReader("src/hitters.tsv"));
			while ((row = hitterReader.readLine()) != null) {
				String[] playerInfo = row.split("\\t");
				fName = playerInfo[0];
				lName = playerInfo[1];
				playerPos = Position.Parse(playerInfo[2]);
				playerTeam = playerInfo[3];
				playerAtBat = Integer.valueOf(playerInfo[4]);
				battingAve = Float.valueOf(playerInfo[5]);
				slugPerc = Float.valueOf(playerInfo[6]);
				onBasePerc = Float.valueOf(playerInfo[7]);
				playerStolenBases = Integer.valueOf(playerInfo[8]);

				Player hitter = new Player(fName, lName, playerPos, playerTeam, playerAtBat, battingAve, slugPerc,
						onBasePerc, playerStolenBases);

				CurrentDraft.AvailablePlayers.Players.add(hitter);
				CurrentDraft.AllPlayers.Players.add(hitter);
			}
			hitterReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader pitcherReader = new BufferedReader(new FileReader("src/pitchers.tsv"));
			while ((row = pitcherReader.readLine()) != null) {
				String[] pPlayerInfo = row.split("\\t");
				pFName = pPlayerInfo[0];
				pLName = pPlayerInfo[1];
				pPlayerPos = Position.Parse(pPlayerInfo[2]);
				pPlayerTeam = pPlayerInfo[3];
				pGamesPlayed = Integer.valueOf(pPlayerInfo[4]);
				pInningsPitched = Float.valueOf(pPlayerInfo[5]);
				pWalksAllowed = Integer.valueOf(pPlayerInfo[6]);
				pEarnedRunAverage = Float.valueOf(pPlayerInfo[7]);
				pGamesStarted = Integer.valueOf(pPlayerInfo[8]);

				Player pitcher = new Player(pFName, pLName, pPlayerPos, pPlayerTeam, pGamesPlayed, pInningsPitched,
						pWalksAllowed, pEarnedRunAverage, pGamesStarted);
				CurrentDraft.AvailablePlayers.Players.add(pitcher);
			}
			pitcherReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
