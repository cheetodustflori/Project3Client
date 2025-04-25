package ui;

import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import network.Client;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class GamePlay {
    private boolean isMyTurn = false;
    private boolean isPlayer1 = false;
    private Client client;
    private Image player1Icon;
    private Image player2Icon;


    int rows = 6;
    int cols = 6;
    Circle[][] coinGrid = new Circle[rows][cols];
    int[][] boardState = new int[rows][cols];
    private StackPane[][] cellGrid = new StackPane[rows][cols];

    boolean isRedTurn = true;

    private Image playerIcon;

    public void setClientAndIcons(Client client, boolean isPlayer1, Image player1Icon, Image player2Icon) {
        this.client = client;
        this.isPlayer1 = isPlayer1;
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
        this.playerIcon = isPlayer1 ? player1Icon : player2Icon;
        this.isMyTurn = isPlayer1;

        // Start listening for opponent's moves
        listenForMoves();
    }


    public void setPlayerIcon(Image icon) {
        this.playerIcon = icon;
    }

    public Parent getRoot(){

            Image title = new Image("/images/logo.png", true);
            ImageView titleContainer = new ImageView(title);
            Image board = new Image("/images/board.png", true);
            ImageView boardContainer = new ImageView(board);

            VBox leftColumn = new VBox(titleContainer);
            leftColumn.setAlignment(Pos.TOP_CENTER);
            leftColumn.setSpacing(20);
            leftColumn.setPrefWidth(200);

            VBox chat = new VBox(new Text("Chat coming soon..."));
            chat.setAlignment(Pos.CENTER);
            chat.setPrefWidth(200);

            GridPane boardGrid = createBoardGrid();
            VBox boardView = new VBox(boardGrid);
            boardView.setAlignment(Pos.CENTER);
            boardView.setPrefWidth(600);

            HBox gamePage = new HBox(leftColumn, boardView, chat);
            gamePage.setSpacing(20);
            gamePage.setAlignment(Pos.CENTER);

            return new StackPane(gamePage);
    }

    private GridPane createBoardGrid() {
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(10);
        boardGrid.setVgap(10);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Circle circle = new Circle(30);
                circle.setFill(javafx.scene.paint.Color.TRANSPARENT);
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                coinGrid[row][col] = circle;

                StackPane cell = new StackPane(circle);
                cell.setPrefSize(70, 70); // Optional: makes clicking easier

                cellGrid[row][col] = cell;

                int currentCol = col; // must be final or effectively final
                cell.setOnMouseClicked(e -> handleMove(currentCol));

                boardGrid.add(cell, col, row);
            }
        }

        return boardGrid;
    }

    private void handleMove(int col) {
        if (!isMyTurn) {
            System.out.println("Not your turn!");
            return;
        }

        for (int row = rows - 1; row >= 0; row--) {
            if (boardState[row][col] == 0) {
                boardState[row][col] = isPlayer1 ? 1 : 2;

                // Update GUI
                Circle circle = coinGrid[row][col];
                circle.setVisible(false);

                ImageView icon = new ImageView(playerIcon);
                icon.setFitHeight(60);
                icon.setFitWidth(60);
                cellGrid[row][col].getChildren().add(icon);

                // Send move to opponent
                if (client != null) {
                    client.send("MOVE:" + col);
                }

                isMyTurn = false;
                break;
            }
        }
    }

    private void listenForMoves() {
        Thread listener = new Thread(() -> {
            while (true) {
                try {
                    String msg = client.in.readObject().toString();
                    if (msg.startsWith("MOVE:")) {
                        int col = Integer.parseInt(msg.split(":")[1]);
                        handleOpponentMove(col);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listener.setDaemon(true);
        listener.start();
    }


    private void handleOpponentMove(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (boardState[row][col] == 0) {
                boardState[row][col] = isPlayer1 ? 2 : 1;

                Circle circle = coinGrid[row][col];
                circle.setVisible(false);

                ImageView icon = new ImageView(isPlayer1 ? player2Icon : player1Icon);
                icon.setFitHeight(60);
                icon.setFitWidth(60);
                cellGrid[row][col].getChildren().add(icon);

                isMyTurn = true;
                break;
            }
        }
    }




//    public class chatWindow(){
//
//    }
}
