import javax.swing.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import Models.PlayerModel;

public class ClientLobbyPanel extends JPanel {
	private static final long serialVersionUID = -4190798695389804885L;
	// Countdown
	private JLabel countdownLabel;
	private static int countdownNum = 30;
	private static final String TIME_UNTIL_START_STRING = "Seconds until start: ";
	private Timer countdownTimer;
	private static final int ONE_SECOND = 1000;
	// Users in lobby
	private JTextArea usersTextArea;
	private static final String PLAYERS_IN_LOBBY_STRING = "Players in Lobby:\n\n";
	private ArrayList<String> usersList;

	//// Constructor ////
	public ClientLobbyPanel() {
		//// Initial GUI setup ////
		setLayout(null);
		setSize(1000, 800);
		setBackground(Color.white);
		
		//// Countdown Setup ////
		countdownLabel = new JLabel(TIME_UNTIL_START_STRING + countdownNum);
		Dimension countdownLabelDimensions = countdownLabel.getPreferredSize();
		countdownLabel.setBounds(700, 300, countdownLabelDimensions.width, countdownLabelDimensions.height);
		add(countdownLabel);
		
		countdownTimer = new Timer(ONE_SECOND, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				countdownNum--;
				updateCountdown();
			} // end public void actionPerformed(ActionEvent)
		});

		//// Users in lobby setup ////
		usersList = new ArrayList<String>();
		usersTextArea = new JTextArea(PLAYERS_IN_LOBBY_STRING);
		usersTextArea.setEditable(false);
		usersTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
		Dimension usersTextAreaDimensions = usersTextArea.getPreferredSize();
		usersTextArea.setBounds(100, 300, usersTextAreaDimensions.width, usersTextAreaDimensions.height);
		add(usersTextArea);
		
		// Final GUI Setup
	} // end public ClientLobbyPanel constructor

	//// Player List Methods ////
	public void addPlayers(Map<Integer, PlayerModel> players) {
		for(Map.Entry<Integer, PlayerModel> entry : players.entrySet()) {
			PlayerModel player = entry.getValue();
			usersList.add("Player: " + player.playerName + " - Team: " + player.playerID);
			updatePlayers();
		}
	} // end public void addPlayers(Map<Integer, PlayerModel>)

	private void updatePlayers() {
		usersTextArea.setText(PLAYERS_IN_LOBBY_STRING);
		for(String username : usersList) {
			// Add player name
			if(usersList.indexOf(username) != 3) {
				username += "\n\n";
			}
			usersTextArea.setText(usersTextArea.getText() + username);

			// Update text area dimensions and repaint it
			Dimension usersTextAreaDimensions = usersTextArea.getPreferredSize();
			usersTextArea.setBounds(100, 300, usersTextAreaDimensions.width, usersTextAreaDimensions.height);
			repaint();
		}
	} // end public void updatePlayers

	public void removePlayer(String player) {
		usersList.remove(player);
		updatePlayers();
	} // end public void removePlayer(String)

	//// Countdown Methods ////
	public void startCountdown() {
		countdownTimer.start();
	} // end public void startCountdown
	
	public void setCountdown(int newCountdown) {
		countdownNum = newCountdown;
		updateCountdown();
	} // end public void setCountdown(int)
	
	private void updateCountdown() {
		if(countdownNum > 0) {
			countdownLabel.setText(TIME_UNTIL_START_STRING + countdownNum);
		}
	} // end public void updateCountdown
} // end class ClientLobbyPanel