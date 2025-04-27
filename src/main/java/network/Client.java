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
//				callback.accept(msg.toString());

//				if (msg.getType().equals("boardUpdate")) {
//					int[][] board = (int[][]) msg.getContent();
//					if (gamePlay != null) {
//						Platform.runLater(() -> gamePlay.updateBoard(board));
//					}
//				}
				if (msg.getType().equals("partnerFound")) {
					Platform.runLater(() -> {
						ArrayList<Player> matchedPlayers = (ArrayList<Player>) msg.getContent();
						Player playerA = matchedPlayers.get(0);
						Player playerB = matchedPlayers.get(1);
						// Identify myself
						Player me = (player.getUsername().equals(playerA.getUsername())) ? playerA : playerB;
						Player opponent = (me == playerA) ? playerB : playerA;

						// Always set myself to start turn = true
//						me.setIsTurn(true);
//						opponent.setIsTurn(false);

						PartnerFound partnerFound = new PartnerFound(me, opponent, this);
						SceneManager.switchTo(partnerFound.getRoot());
					});
				}
				else if (msg.getType().equals("move")) {
					int[] move = (int[]) msg.getContent();
					int row = move[0];
					int col = move[1];
					String playerWhoMoved = msg.getSender();
					System.out.print(playerWhoMoved);
					Platform.runLater(() -> {
						gamePlay.updateBoardCell(row,col,playerWhoMoved);
					});
				}
				else if(msg.getType().equals("yourTurn")){
					boolean isMyTurn = (Boolean)msg.getContent();
					gamePlay.setMyTurn(isMyTurn);
				}
				else if(msg.getType().equals("loginError")){
					String message = msg.getContent().toString();
					if(loginPage != null){
						loginPage.showError(message);
					}
				}
				else if(msg.getType().equals("loginSuccess")){
					Platform.runLater(() -> {
						Home home = new Home(player, this);
						SceneManager.switchTo(home.getRoot());
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
