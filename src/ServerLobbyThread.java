import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Models.PlayerModel;


public class ServerLobbyThread extends Thread{
	boolean running = true;
	private Socket s;
	int id;
	GameServer parent;
	BufferedReader br;
	boolean team1 = true;
	String finalUsername = null;
	
	public ServerLobbyThread(Socket s, int id, GameServer parent) {
		this.s = s;
		this.id = id;
		this.parent = parent;
		
		try {
			br = new BufferedReader( new InputStreamReader(s.getInputStream() ) );
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void run() {
				//sql testinfo
				int port =9999;
				String hostname = "localhost";
				
				String originalName = null;
				PrintWriter pw = parent.allClientWriters.get(id);
				try {originalName = br.readLine();} catch (IOException e) {} 
				//USERNAME:USERNAME_HERE
				//System.out.println( "Line received from client " + s.getInetAddress() + ": " + line );
				
				//listens for the username they want to have
				//lookup in mysql database of existing names
				//if the username is unique, the player is confirmed
				//send result to client (unique/not unique)	
				SQLCommand c = new SQLCommand(parent.queryLock, this, originalName);
				c.run();
				pw.println( "USERNAME:" + finalUsername);
				pw.flush();
				
				PlayerModel player = new PlayerModel(id, finalUsername, team1);
					//creates a PlayerModel	
					//assigns the PlayerModel to a random team team1 or team2
					if(team1 == true) {
						parent.team1.add(player);
						flipTeam();
					} else {
						parent.team2.add(player);
						flipTeam();
					}
					//Broadcast that a new player has joined
					//broadcast the time left in Lobby = 30
					String announcement = "CHAT:Broadcast:player "+finalUsername+ "has joined the game";
					String timerPush = "TIMER:30";
					for(int i =0; i<parent.allClientWriters.size();i++) {
						parent.allClientWriters.get(i).println(announcement);
						parent.allClientWriters.get(i).flush();
						parent.allClientWriters.get(i).println(timerPush);
						parent.allClientWriters.get(i).flush();
					}
					
					//status:ready
					//Listen for player starting game confirmation
					String status = null;
					try {status = br.readLine();} catch (IOException e) {e.printStackTrace();} 
					if(status.equals("STATUS:READY") && !parent.gameStart) {
						parent.readyState[id] = true;
						if(allClientsReady()) {
							parent.gameState[0] = false;
							parent.gameState[1] = true;
							parent.gameStart = true;
						}
						
					}
				
	}
	//last client to connect will start off the game
	private boolean allClientsReady() {
		for(int i =0; i< id; i++) {
			if(parent.readyState[i] == false) {
				return false;
			}
		}
		return true; 
	}

	void flipTeam() {
		if(team1 == true) {
			team1 = false;
		} else {
			team1 = true;
		}
	}
	
}