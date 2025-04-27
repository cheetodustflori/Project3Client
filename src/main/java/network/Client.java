package network;

import javafx.application.Platform;
import ui.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Client extends Thread {

	private Socket socketClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int clientCount;
	private GamePlay gamePlay;
	private Player player;
	private Login loginPage;

	private Consumer<Serializable> callback;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setGamePlay(GamePlay gamePlay) {
		this.gamePlay = gamePlay;
	}

	public void setLoginPage(Login loginPage){
		this.loginPage = loginPage;
	}

	//	RUN
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

				if (msg == null || msg.getType() == null) continue; // Safeguard

				switch (msg.getType()) {
					case PARTNERFOUND:
						Platform.runLater(() -> {
							ArrayList<Player> matchedPlayers = (ArrayList<Player>) msg.getContent();
							Player playerA = matchedPlayers.get(0);
							Player playerB = matchedPlayers.get(1);

							Player me = (player.getUsername().equals(playerA.getUsername())) ? playerA : playerB;
							Player opponent = (me == playerA) ? playerB : playerA;

							PartnerFound partnerFound = new PartnerFound(me, opponent, this);
							SceneManager.switchTo(partnerFound.getRoot());
						});
						break;

					case UPDATEMOVE: // (Previously your "move" handler)
						int[] move = (int[]) msg.getContent();
						int row = move[0];
						int col = move[1];
						String playerWhoMoved = msg.getSender();
						System.out.println(playerWhoMoved);
						Platform.runLater(() -> {
							gamePlay.updateBoardCell(row, col, playerWhoMoved);
						});
						break;

					case YOURTURN:
						boolean isMyTurn = (Boolean) msg.getContent();
						gamePlay.setMyTurn(isMyTurn);
						System.out.println("YOUR TURN");
						break;

					case LOGINERROR:
						String errorMsg = msg.getContent().toString();
						if (loginPage != null) {
							Platform.runLater(() -> {
								loginPage.showError(errorMsg);
							});
						}
						break;

					case LOGINSUCCESS:
						Platform.runLater(() -> {
							Home home = new Home(player, this);
							SceneManager.switchTo(home.getRoot());
						});
						break;

					case CHAT:
						String sender = msg.getSender();
						String text = msg.getContent().toString();
						if (gamePlay != null) {
							System.out.println("TESTING IN CHAT CLIENT");
							gamePlay.addChatMessage(sender + ": " + text);
						}
						break;




					default:
						System.out.println("Unknown message type received: " + msg.getType());
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
			Message playerInfo = new Message(MessageType.PLAYERINFO, player, player.getUsername(), null);
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
