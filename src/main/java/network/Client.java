package network;

import javafx.application.Platform;
import ui.GamePlay;
import ui.PartnerFound;
import ui.SceneManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

	private Socket socketClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private int clientCount;

	private GamePlay gamePlay;
	private Player player;

	// Constructor
	public Client() {}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setGamePlay(GamePlay gamePlay) {
		this.gamePlay = gamePlay;
	}

	@Override
	public void run() {
		try {
			socketClient = new Socket("127.0.0.1", 5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				Message msg = (Message) in.readObject();

				if (msg.getType().equals("boardUpdate")) {
					int[][] board = (int[][]) msg.getContent();
					if (gamePlay != null) {
						Platform.runLater(() -> gamePlay.updateBoard(board));
					}
				} else if (msg.getType().equals("partnerFound")) {
					Platform.runLater(() -> {
						ArrayList<Player> matchedPlayers = (ArrayList<Player>) msg.getContent();
						Player playerA = matchedPlayers.get(0);
						Player playerB = matchedPlayers.get(1);

						// Identify myself
						Player me = (player.getUsername().equals(playerA.getUsername())) ? playerA : playerB;
						Player opponent = (me == playerA) ? playerB : playerA;

						// Always set myself to start turn = true
						me.setIsTurn(true);
						opponent.setIsTurn(false);

						PartnerFound partnerFound = new PartnerFound(me, opponent, this);
						SceneManager.switchTo(partnerFound.getRoot());
					});
				}
				else if (msg.getType().equals("move")) {
					int col = (Integer) msg.getContent();
					Platform.runLater(() -> {
						if (gamePlay != null) {
							gamePlay.handleOpponentMove(col);
						}
					});
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Methods to send messages
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPlayer(Player player) {
		try {
			Message playerInfo = new Message("playerInfo", player, player.getUsername(), null);
			out.writeObject(playerInfo);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Socket getSocket() {
		return socketClient;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public int getClientCount(){return clientCount;}
}
