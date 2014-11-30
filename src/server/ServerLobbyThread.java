package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import Models.PlayerModel;


public class ServerLobbyThread extends Thread{
	boolean running = true;
	int id;
	GameServer parent;
	boolean team1 = true;
	int team = 1;
	String finalUsername = null;
	
	public ServerLobbyThread(int id, GameServer parent) {
		//this.s = s;
		this.id = id;
		this.parent = parent;
	}
	
	public void run() {
				//sql testinfo
				//int port =9999;
				//String hostname = "localhost";
				
				String originalName = null;
				ObjectOutputStream oos = parent.allClientObjectWriters.get(id);
				try {
					originalName = (String)parent.allClientObjectReaders.get(id).readObject();
					
				} 
				catch (IOException e) {} 
				catch (ClassNotFoundException e) {e.printStackTrace();}
				
				//USERNAME:USERNAME_HERE
				//System.out.println( "Line received from client " + s.getInetAddress() + ": " + line );
 
				
				//listens for the username they want to have
				//lookup in mysql database of existing names
				//if the username is unique, the player is confirmed
				//send result to client (unique/not unique)	
				SQLCommand c = new SQLCommand(parent.queryLock, this, originalName);
				c.run();
				//finalUsername = "Matias#95";
				String msg = "USERNAME:" + finalUsername;
				try {
					oos.writeObject(msg);
					//oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(team1 == true) {
					team = 1;
				} else {
					team =2;
				}
				PlayerModel player = new PlayerModel(id, finalUsername, team);
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
					String announcement = "CHAT:Broadcast:"+finalUsername;
					String timerPush = "TIMER";
					Set<Integer> pwSet = parent.allClientObjectWriters.keySet();
					Iterator<Integer> itPrint = pwSet.iterator();
					while(itPrint.hasNext())
					{
						int nextKey = itPrint.next();
						try {
							parent.allClientObjectWriters.get(nextKey).writeObject(announcement);
							parent.allClientObjectWriters.get(nextKey).writeObject(timerPush);

						} catch (IOException e) {e.printStackTrace();}
						//parent.allClientWriters.get(nextKey).flush();
						//parent.allClientWriters.get(nextKey).flush();
					}
					/*
					for(int i =0; i<parent.allClientWriters.size();i++) {
						parent.allClientWriters.get(i).println(announcement);
						parent.allClientWriters.get(i).flush();
						parent.allClientWriters.get(i).println(timerPush);
						parent.allClientWriters.get(i).flush();
					}*/
					
					//status:ready
					//Listen for player starting game confirmation
					String status = null;
					try {
						status = (String) parent.allClientObjectReaders.get(id).readObject();
					}
					catch (ClassNotFoundException e) {e.printStackTrace();} 
					catch (IOException e) {e.printStackTrace();} 
				
					System.out.println(status);
					if(status.contains("STATUS:READY") && !parent.allPlayersReady) {
						parent.readyState[id] = true;
						if(allClientsReady()) {
							
							////make thread safe method//
							parent.gameState[0] = false;
							parent.gameState[1] = true;
							//parent.gameStart = true;
							/////////////////////////////
							/////alternative/////////////
							//parent.changeState(0, 1);
							parent.setPlayersReady();
							/////////////////////////////
							/*
							for(int i =0; i<parent.allClientWriters.size();i++) {
								parent.allClientWriters.get(i).println("STATUS:START");
								parent.allClientWriters.get(i).flush();
							}*/
							pwSet = parent.allClientObjectWriters.keySet();
							itPrint = pwSet.iterator();
							while(itPrint.hasNext())
							{
								int nextKey = itPrint.next();
								try {
									parent.allClientObjectWriters.get(nextKey).writeObject("STATUS:START");
								} 
								catch (IOException e) {e.printStackTrace();}
								
								//parent.allClientObjectWriters.get(nextKey).flush();
							}
						}
						
					}
				
	}
	//last client to connect will start off the game
	private boolean allClientsReady() {
		/*for(int i =0; i< id; i++) {
			if(parent.readyState[i] == false) {
				return false;
			}
		}*/
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