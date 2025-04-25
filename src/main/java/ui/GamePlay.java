package ui;

import javafx.application.Application;

import javafx.geometry.Insets;
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
    private boolean isMyTurn = true;
    private boolean isPlayer1 = false;
    private Client client;
    private Image player1Icon;
    private Image player2Icon;

    int rows = 6;
    int cols = 6;
    Circle[][] coinGrid = new Circle[rows][cols];
    int[][] boardState = new int[rows][cols];
    private StackPane[][] cellGrid = new StackPane[rows][cols];

    private Image playerIcon;

    public GamePlay(Client client) {
        this.client = client;
    }

    public void setClientAndIcons(Client client, boolean isPlayer1, Image player1Icon, Image player2Icon) {
        this.client = client;
        this.isPlayer1 = isPlayer1;
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
        this.playerIcon = isPlayer1 ? player1Icon : player2Icon;
        this.isMyTurn = isPlayer1;

        listenForMoves();
    }

    public void setPlayerIcon(Image icon) {
        this.playerIcon = icon;
    }

    public Parent getRoot(){

            Image title = new Image("/images/logo.png", true);
            ImageView titleContainer = new ImageView(title);
            titleContainer.setFitWidth(180);
            titleContainer.setFitHeight(100);
            VBox.setMargin(titleContainer, new Insets(50, 0, 0, 0)); // top, right, bottom, left


        VBox leftColumn = new VBox(titleContainer);
            leftColumn.getStyleClass().add("left-column");
            leftColumn.setAlignment(Pos.TOP_CENTER);
            leftColumn.setSpacing(20);
            leftColumn.setMinWidth(200);
            leftColumn.setMaxWidth(200);

            VBox chat = new VBox(new Text("Chat coming soon..."));
            chat.getStyleClass().add("chat");
            chat.setAlignment(Pos.CENTER);
            chat.setPrefWidth(300);
            chat.setMinWidth(300);

            GridPane boardGrid = createBoardGrid();
            boardGrid.getStyleClass().add("board-grid");

            Text playerOneYou = new Text("Player One (You)");
            ImageView player1IconView = new ImageView(player1Icon);
            HBox player1 = new HBox(playerOneYou, player1IconView);

            Text playerTwo = new Text("Player Two (@other_username)");
            ImageView player2IconView = new ImageView(player2Icon);
            HBox player2 = new HBox(playerTwo, player2IconView);

            HBox playerNames = new HBox(player1, player2);

            Text playerTurn = new Text("@other_username's Turn");
            VBox boardView = new VBox(playerTurn, boardGrid, playerNames);
            boardView.getStyleClass().add("board-view");
            boardView.setAlignment(Pos.CENTER);
            boardView.setPrefWidth(580);

            HBox.setHgrow(leftColumn, Priority.ALWAYS);
            HBox.setHgrow(chat, Priority.ALWAYS);
            HBox.setHgrow(boardView, Priority.NEVER);

            HBox gamePage = new HBox(leftColumn, boardView, chat);

            gamePage.getStyleClass().add("game-page");
            gamePage.setSpacing(20);
            gamePage.setAlignment(Pos.CENTER);

//            ROOT
            StackPane root = new StackPane(gamePage);
            root.setPrefSize(1280,832);
            root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            return root;
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
                circle.setStroke(javafx.scene.paint.Color.WHITE);
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

//                isMyTurn = false;
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
