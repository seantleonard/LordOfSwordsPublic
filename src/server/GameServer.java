import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Models.GridMapModel;
import Models.PlayerModel;

/*				CLIENT-SERVER Communication Nomenclature
 * 				Standard policy: all caps, delimited by colons, no spaces.
 * -Lobby	"USERNAME:theactualusername"	"STATUS:READY" "TIMER:timetoadd"
 * -chat    -server receives "CHAT:recipient1,recipient2,recipient3:messageContent"
 * 			 -server sends	   "CHAT:senderName:messageContent"
 * 
 * 
 * 
 * 
 * 						ADD YOUR OWN SO EVERYONE CAN STAY IN SYNC
 */

public class GameServer {
	////////////////////////////////////ALL SHARED DATA////////////////////////////////////
	boolean gameState[] = {true, false, false}; //0: joining game  1:game play  2:game over
	GridMapModel gmm;
	Map<Integer, PrintWriter> allClientWriters = new HashMap<Integer, PrintWriter>();
	Map<Integer, ObjectOutputStream> allClientObjectWriters = new HashMap<Integer, ObjectOutputStream>();
	Set<PlayerModel> team1 = new HashSet<PlayerModel>();
	Set<PlayerModel> team2 = new HashSet<PlayerModel>();
	int timeLeftInLobby = 30;
	int id = 0;
	boolean gameStart = false;
	boolean allPlayersReady = false;
	boolean gridMapInit = false;
	boolean readyState[] = {false, false, false, false}; //fixed at player cap
	///////////////////////////////////////////////////////////////////////////////////////
	
	//////////////LOCKS///////////////////////////
	ReentrantLock queryLock = new ReentrantLock();
	Lock stateLock = new ReentrantLock();
	//////////////////////////////////////////////

	//Threads
	ArrayList<ServerLobbyThread> lobbyThreads = new ArrayList<ServerLobbyThread>();
	ArrayList<ServerGameThread> gameThreads = new ArrayList<ServerGameThread>();
	ArrayList<ServerUpdateClientThread> updateClientThreads = new ArrayList<ServerUpdateClientThread>(); 

	//////////////////CONNECT TO SERVER///////////////////////////
	public GameServer(){	
		try{
			ServerSocket ss= new ServerSocket(5001);
			while(true) {
				//now listening
				new ServerThread(ss.accept(), ++id, this).start();			
			}
		} 
		catch (IOException ioe) {}
	}
	/////////////////////////////////////////////////////////////
	
	public void isGridMapInit(){
		stateLock.lock();
		gridMapInit = true;
		stateLock .unlock();
	}
	
	public void changeState(int current, int next){
		stateLock.lock();
		gameState[next] = true;
		gameState[current] = false;	
		stateLock.unlock();
	}
	
	public void setGameStart()
	{
		stateLock.lock();
		gameStart = true;
		stateLock.unlock();
	}
	
	public void setPlayersReady()
	{
		stateLock.lock();
		allPlayersReady = true;
		stateLock.unlock();
	}
	
	///////////////STARTING SERVER///////////
	public static void main(String[] args) {
		GameServer s = new GameServer();
	}
	/////////////////////////////////////////
	
	
	////////////////////INITIAL SERVER THREAD////////////////////
	/*	created for each instance of a connection
	 *	controls flow of game-play
	 * 	creates additional threads
	 */
	public class ServerThread extends Thread
	{
		Socket s;
		int id;
		GameServer gs;
		PrintWriter pw = null;
		ObjectOutputStream oos = null;
		
		public ServerThread(Socket s, int id, GameServer gs){
			try {
				this.s = s;
				this.id = id;
				this.gs = gs; 
				pw = new PrintWriter(s.getOutputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				allClientWriters.put(id, pw);
				allClientObjectWriters.put(id, oos);
			} 
			catch (IOException e) {}
			
			//start the login/lobby
			ServerLobbyThread slt = new ServerLobbyThread(s, id, gs);
			lobbyThreads.add(slt);
			slt.start();
		}
		
		public void run(){
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				System.out.println("tities");
				if(gameState[0]) {
					//still in lobby, do nothing until all threads end
					System.out.println("0");
				}
				else if(gameState[1]) {
					//kill all ServerLobbyThread threads (should be dead)
					//this code will only run once
					System.out.println("1 asdfsdf");
	
					if(!gameStart){
						System.out.println("init 1111");
						//start all ServerGameThread threads
						ServerGameThread sgt = new ServerGameThread(s, id, gs);
						gameThreads.add(sgt);
						sgt.start();
							
						//start all ServerUpdateClientThread threads
						ServerUpdateClientThread suc = new ServerUpdateClientThread(s, id, gs);
						updateClientThreads.add(suc);
						suc.start();
						setGameStart();
					}
				}
				else if(gameState[2]){
					//kill all ServerGameThread/ServerUpdateClientThreads threads (should be dead)
					//the game is finished
				}
			}
		}
	}

}