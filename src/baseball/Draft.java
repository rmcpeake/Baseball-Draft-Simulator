package baseball;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;

public class Draft {
	public HashMap<String, PlayerCollection> LeagueMembers;
	public PlayerCollection AvailablePlayers;
	public PlayerCollection UnavailablePlayers;
	public PlayerCollection AllPlayers;
	public int DraftCounter;
	public String EvalFunExpression;
	public String PEvalFunExpression;

	public char lm;

	public Draft() {
		AvailablePlayers = new PlayerCollection();
		UnavailablePlayers = new PlayerCollection();
		AllPlayers = new PlayerCollection();
		LeagueMembers = new HashMap<String, PlayerCollection>();
		LeagueMembers.put("A", new PlayerCollection());
		LeagueMembers.put("B", new PlayerCollection());
		LeagueMembers.put("C", new PlayerCollection());
		LeagueMembers.put("D", new PlayerCollection());
	}

	public void DraftPlayer(String memberName, String playerName) {
		var player = getPlayer(playerName);
		if (player != null) {
			if (LeagueMembers.get(memberName).CanDraft(player.Position)) {
				player.DraftingOrder = DraftCounter;
				LeagueMembers.get(memberName).Players.add(player);
				UnavailablePlayers.Players.add(player);
				AvailablePlayers.Players.remove(player);
				DraftCounter++;
				System.out.println(playerName + " has been drafted to League " + memberName);
				System.out.println();
			} else {
				System.out.println("League Member " + memberName + " is unable to draft another "
						+ player.Position.getPositionName() + ".");
				System.out.println();
			}
		}
	}

	/***
	 * Saves the current Draft object to a file
	 * 
	 * @return true if the file saved successfully, false if it failed.
	 */
	public boolean save(String userInput) {
		Gson gson = new Gson();
		var outputJson = gson.toJson(this);

		String filename = userInput + ".fantasy.txt";
		try {
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(outputJson);
			fileWriter.close();
			System.out.println("Draft progress has been saved in " + filename + ".");
			System.out.println();
			return true;
		} catch (IOException e) {
			System.out.println("Save failed.");
			return false;
		}
	}

	public Draft restore(String userInput) {
		Gson gson = new Gson();
		String filename = userInput + ".fantasy.txt";

		try {
			File restoreFile = new File(filename);
			Scanner scanner = new Scanner(restoreFile);
			scanner.useDelimiter("\\Z");

			String inputJson = scanner.next();
			scanner.close();
			var temp = gson.fromJson(inputJson, Draft.class);
			System.out.println("Draft progress has been restored from " + filename + ".");
			return temp;
		} catch (Exception e) {
			System.out.println("Unable to restore draft progress from a file named " + filename + ".");
			return null;
		}

	}

	private Player getPlayer(String playerName) {
		var players = AvailablePlayers.GetPlayersByName(playerName);
		if (players.size() == 0) {
			var unavailblePlayer = UnavailablePlayers.GetPlayerByName(playerName);
			if (unavailblePlayer != null) {
				System.out.println("The player selected is not available to be drafted. No player drafted.");
			} else {
				System.out.println("No player identified with this name. No player drafted.");
			}
			return null;
		} else if (players.size() > 1) {
			System.out.println(
					"Multiple players matched. You will need to search in the format of \"Lastname, Initial\". No player drafted.");
			return null;
		} else {
			return players.get(0);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o.getClass() != Draft.class) {
			return false;
		} else {
			var that = (Draft) o;
			return this.lm == that.lm && this.DraftCounter == that.DraftCounter
					&& this.AllPlayers.equals(that.AllPlayers) && this.LeagueMembers.equals(that.LeagueMembers)
					&& this.AvailablePlayers.equals(that.AvailablePlayers)
					&& this.UnavailablePlayers.equals(that.UnavailablePlayers);
		}

	}

	public PlayerCollection getPC(String lm) {
		return LeagueMembers.get(lm);
	}

	public void PrintRankings() { // currently not removing players after drafting because "allplayers" not
									// available players are being sent in
		ArrayList<Position> positions = this.LeagueMembers.get("A").PositionsAvailable();
		positions.remove(Position.PITCHER);

		ArrayList<Player> playersByPosition = new ArrayList<Player>();
		String valuation = EvalFunExpression== null ? "AVG" : EvalFunExpression;
		System.out.println("Player\t\t\tTeam \tPosition \t" + valuation);
		System.out.println("==========================================================================");

		for (Position position : positions) {
			playersByPosition = this.AvailablePlayers.GetPlayersByPosition(position);
			 playersByPosition.sort(Comparator.comparing(Player::EvalFunctionResult).reversed());
			// //this allows us to sort based on the result of some function on the player
			// object
			for (int p = 0; p < playersByPosition.size(); p++) { // iterate on that list to print all players of that
																	// position
				System.out.print(playersByPosition.get(p).FullName() +(playersByPosition.get(p).FullName().length() < 16? " ".repeat(16 - playersByPosition.get(p).FullName().length()): "" )+ "\t ");
				System.out.print(playersByPosition.get(p).RealTeam + "\t ");
				System.out.print(playersByPosition.get(p).Position + "\t ");
				System.out.printf("%.3f\n", playersByPosition.get(p).EvalFunctionResult());
			}
			System.out.println("==========================================================================");

		}
		System.out.println();
	}

	public void PrintRankings(Position position) {
		ArrayList<Player> temp = new ArrayList<Player>();
		if (!this.LeagueMembers.get("A").CanDraft(position)) {
			System.out.println("Error: Position has already been filled on league, no stats will be displayed.");
		} else {
			String valuation = EvalFunExpression== null ? "AVG" : EvalFunExpression;
			System.out.println("Player\t\t\tTeam \t Position \t" + valuation);																// AVG
			temp = this.AvailablePlayers.GetPlayersByPosition(position);
			temp.sort(Comparator.comparing(Player::EvalFunctionResult).reversed());
			for (int i = 0; i < temp.size(); i++) {
				System.out.print(temp.get(i).FullName() + (temp.get(i).FullName().length() < 16? " ".repeat(16 - temp.get(i).FullName().length()): "") + "\t ");
				System.out.print(temp.get(i).RealTeam + "\t ");
				System.out.print(temp.get(i).Position + "\t ");
				System.out.printf("%.3f\n", temp.get(i).EvalFunctionResult());

			}
			System.out.println();
		}
	}

	public void PrintPitcherRankings() {
		String valuation = PEvalFunExpression== null ? "IP" : PEvalFunExpression;
		System.out.println("Player\t\t\tTeam \t Position \t " + valuation);				
		ArrayList<Player> temp = new ArrayList<Player>();
		temp = this.AvailablePlayers.GetPlayersByPosition(Position.PITCHER);
		temp.sort(Comparator.comparing(Player::EvalFunctionResult).reversed());
		for (int i = 0; i < temp.size(); i++) {
			System.out.print(temp.get(i).FullName() + (temp.get(i).FullName().length() < 16? " ".repeat(16 - temp.get(i).FullName().length()): "") + "\t ");
			System.out.print(temp.get(i).RealTeam + "\t ");
			System.out.print(temp.get(i).Position + "\t ");
			System.out.printf("%.3f\n", temp.get(i).EvalFunctionResult());

		}
		System.out.println();
	}
	
	public void SetEvalFunExpression(String expression) {
		EvalFunExpression = expression;
		System.out.println("Expression has been set. Use the OVERALL command to view.");
		System.out.println();
	}
	
	public void SetPEvalFunExpression(String expression) {
		PEvalFunExpression = expression;
		System.out.println("Expression has been set. Use the POVERALL command to view.");
		System.out.println();

	}
}
