import java.net.Socket;
import javax.swing.JTextArea;

public class ClientLobbyThread extends Thread {
	Socket s;
	JTextArea usersInLobby;
	
	public void run() {
		// Thread listens for information from server
	}
} // end class ClientLobbyThread
