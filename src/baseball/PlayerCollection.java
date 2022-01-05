package baseball;

import java.util.ArrayList;
import java.util.Comparator;

public class PlayerCollection {
	public ArrayList<Player> Players;

	public PlayerCollection() {
		Players = new ArrayList<Player>();
	}

	public boolean CanDraft(Position position) {
		if (position == Position.PITCHER) {
			if (PitcherCount() <= 4) {
				return true;
			} else {
				return false;
			}
		} else {
			return !HasPosition(position);
		}
	}

	public void PrintRoster() {
		String[] arr =  new String[12];
			for(var player : Players) {
				if(player.Position.getPositionName().equals("catcher")) {
					String name = player.Position + " " + player.FullName();
					arr[0] = name;
					
				}else if(player.Position.getPositionName().equals("first baseman")) {
					String name = player.Position + " " + player.FullName();
					arr[1] = name;
					
				}else if(player.Position.getPositionName().equals("second baseman")) {
					String name = player.Position + " " + player.FullName();
					arr[2] = name;
					
				}else if(player.Position.getPositionName().equals("third baseman")) {
					String name = player.Position + " " + player.FullName();
					arr[3] = name;
					
				}else if(player.Position.getPositionName().equals("short stop")) {
					String name = player.Position + " " + player.FullName();
					arr[4] = name;
					
				}else if(player.Position.getPositionName().equals("left fielder")) {
					String name = player.Position + " " + player.FullName();
					arr[5] = name;
					
				}else if(player.Position.getPositionName().equals("center fielder")) {
					String name = player.Position + " " + player.FullName();
					arr[6] = name;
					
				}else if(player.Position.getPositionName().equals("right fielder")) {
					String name = player.Position + " " + player.FullName();
					arr[7] = name;
					
				}else if(player.Position.getPositionName().equals("pitcher")) {
					String name = player.Position + " " + player.FullName();
					if(arr[8] == null) {
						arr[8] = name;
					}else if(arr[9] == null) {
						arr[9] = name;
					}else if(arr[10] == null) {
						arr[10] = name;
					}else if(arr[11] == null) {
						arr[11] = name;
					}else if(arr[12] == null) {
						arr[12] = name;
					}
				}else {
					System.out.println("failure");
					
				}
		}
		System.out.println("Roster printed out in order by position: \n");
			
		int i = 0;
		while(i< arr.length) {
			if (arr[i] != null) {
				System.out.println(arr[i]);
				i++;
			}else {
				System.out.println("-");
				i++;
			}
		}
		System.out.println();
	}

	public void PrintStars() {
		System.out.println("Roster Printed out in draft order \n");
		
		int c = 1;
		var sortedPlayers = Players;
		sortedPlayers.sort(Comparator.comparing(Player::GetDraftingOrder));
		for (var player : sortedPlayers) {
			System.out.println(c + ". " + player.Position + " " + player.FirstName + " " + player.LastName);
			c++;
		}
		System.out.println();

	}

	public void PrintRankings() {
		ArrayList<Position> positions = this.PositionsAvailable();
		positions.remove(Position.PITCHER);
		
		ArrayList<Player> playersByPosition = new ArrayList<Player>();
		System.out.println("Player \t \t Team \t \t Position \t AVG");
		System.out.println("==========================================================================");
	
		for(Position position :positions) {
			playersByPosition = GetPlayersByPosition(position);	// iterate positions into the list
			for (int p = 0; p < playersByPosition.size(); p++) {	// iterate on that list to print all players of that position
				System.out.print(playersByPosition.get(p).FullName() + "\t ");
				System.out.print(playersByPosition.get(p).RealTeam + "\t ");
				System.out.print(playersByPosition.get(p).Position + "\t ");
				System.out.println(playersByPosition.get(p).HitterStats.BattingAverage); // placeholder for PEVALFUN data
			}
			System.out.println("==========================================================================");
		
		}
		System.out.println();
	}

	public ArrayList<Player> GetPlayersByName(String name) {
		var tempList = new ArrayList<Player>();
		for (var player : Players) {
			if (player.LastName.toLowerCase().equals(name.toLowerCase()) || player.LastNameFirstInitial().toLowerCase().equals(name.toLowerCase())
					|| player.FullName().toLowerCase().equals(name.toLowerCase())) {
				tempList.add(player);
			}
		}
		return tempList;
	}

	public Player GetPlayerByName(String name) {
		for (var player : Players) {
			if (player.LastName.toLowerCase().equals(name.toLowerCase()) || player.LastNameFirstInitial().toLowerCase().equals(name.toLowerCase())
					|| player.FullName().toLowerCase().equals(name.toLowerCase())) {
				return player;
			}
		}
		return null;
	}

	public ArrayList<Player> GetPlayersByPosition(Position position) {
		var tempList = new ArrayList<Player>();
		for (var player : Players) {
			if (player.Position.equals(position)) {
				tempList.add(player);
			}
		}
		return tempList;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}else if (!(o instanceof PlayerCollection)) {
			return false;
		}else {
			PlayerCollection that = (PlayerCollection)o;
			return this.Players.equals(that.Players);
		}
	}
	
	private int PitcherCount() {
		int count = 0;
		for (var player : Players) {
			if (player.Position == Position.PITCHER) {
				count++;
			}
		}
		return count;
	}

	private boolean HasPosition(Position position) {
		for (var player : Players) {
			if (player.Position == position) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Position> PositionsAvailable(){
		var positions = Position.values();
		var availablePositions = new ArrayList<Position>();
		for (Position position : positions) {
			if(!this.HasPosition(position)) {
				availablePositions.add(position);
			}
		}
		return availablePositions;
	}
}
