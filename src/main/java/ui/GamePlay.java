package ui;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
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
import network.Message;
import network.MessageType;
import network.Player;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;


public class GamePlay {
    Player player1;
    Player player2;
    Client client;

    int rows = 6;
    int cols = 6;

    Circle[][] coinGrid = new Circle[rows][cols];
    int[][] boardState = new int[rows][cols];
    private StackPane[][] cellGrid = new StackPane[rows][cols];
    private GridPane board;

    private VBox chatBox = new VBox();  // this holds all messages
    private TextField chatInput = new TextField();  // user types here
    private Button sendButton = new Button("Send");

    // DEFAULT IMAGE VALUES
    private Image playerIcon1 = new Image("images/apple.png");
    private ImageView playerIcon1View = new ImageView(playerIcon1);
    private Image playerIcon2 = new Image("images/apple.png");
    private ImageView playerIcon2View = new ImageView(playerIcon2);

    Text playerTurnText = new Text("");

//    public void updateBoard(int[][] newBoard){
//        player1.setIsTurn(true);
//        System.out.print(player1 + " inside update board");
//        this.boardState = newBoard;
//        redrawBoard(row);
//    }

    public void addChatMessage(String text) {
        Text messageText = new Text(text);
        chatBox.getChildren().add(messageText);
    }


    private boolean myTurn = false;
    public void setMyTurn(boolean turn){

        System.out.println("MY TURN");
        this.myTurn = turn;
    }

    public void updateBoardCell(int row, int col, String playerUsername){
        boardState[row][col] = playerUsername.equals(player1.getUsername()) ? 1 : 2;
        redrawBoard(row,col,playerUsername);
    }

    private void redrawBoard(int row, int col, String playerUsername) {
        ImageView icon = new ImageView();
        icon.setFitHeight(60);
        icon.setFitWidth(60);
        if(player1.getUsername().equals(playerUsername)){
            icon = new ImageView(playerIcon1);
        }
        else {
            icon = new ImageView(playerIcon2);
        }
        Circle circle = coinGrid[row][col];
        circle.setVisible(false);
        cellGrid[row][col].getChildren().add(icon);
        playerTurnText.setText("Your Turn");
    }

    private void handleMove(int col) {
        int[] playerMove = new int[] {0,0};
        if (!myTurn) {
            System.out.println("Not your turn!");
            return;
        }

        int row;
        for (row = rows - 1; row >= 0; row--) {
            if (boardState[row][col] == 0) {
                boardState[row][col] = player1.getIsTurn() ? 1 : 2;

                Circle circle = coinGrid[row][col];
                playerMove = new int[] {row,col};
                circle.setVisible(false);

                ImageView icon = new ImageView(playerIcon1);
                icon.setFitHeight(60);
                icon.setFitWidth(60);
                cellGrid[row][col].getChildren().add(icon);
                player1.setIsTurn(false);
                if (player2 != null) {
                    player2.setIsTurn(true);
                }
                playerTurnText.setText(player2.getUsername() + "'s Turn");
                break;
            }
        }
        String updateMessage = player1.getUsername() + " made the move to row: " + row + " col: " + col;
        client.sendMessage(new Message(MessageType.TEXT, updateMessage));
        client.sendMessage(new Message(MessageType.GAMEMOVE, playerMove, player1.getUsername(), null));

    }

//    public void handleOpponentMove(int col) {
//        for (int row = rows - 1; row >= 0; row--) {
//            if (boardState[row][col] == 0) {
//                boardState[row][col] = 2; // Opponent's move
//                Circle circle = coinGrid[row][col];
//                circle.setVisible(false);
//
//                ImageView icon = new ImageView(playerIcon2); // opponent's icon
//                icon.setFitHeight(60);
//                icon.setFitWidth(60);
//                cellGrid[row][col].getChildren().add(icon);
//
//                player1.setIsTurn(true); // After opponent moves, it's now MY turn
//                playerTurnText.setText("Your Turn!");
//                break;
//            }
//        }
//    }


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
                cell.setPrefSize(70, 70);

                cellGrid[row][col] = cell;

                int currentCol = col;
                cell.setOnMouseClicked(e -> handleMove(currentCol));

                boardGrid.add(cell, col, row);
            }
        }

        return boardGrid;
    }

    public GamePlay(Player player1, Player player2, Client client){
        this.player1 = player1;
        this.player2 = player2;
        playerIcon1 = new Image(player1.getIcon());
        playerIcon2 = new Image(player1.getIcon());
        playerIcon1View = new ImageView(playerIcon1);
        playerIcon1View.setFitHeight(70);
        playerIcon1View.setFitWidth(70);
        playerIcon2View = new ImageView(playerIcon2);
        playerIcon2View.setFitHeight(70);
        playerIcon2View.setFitWidth(70);
        System.out.println(player1.getIsTurn());
        System.out.println(player2.getIsTurn());
        this.client = client;
        client.sendMessage(new Message(MessageType.BOARDUPDATE, boardState, player1.getUsername(), null));
    }

    public Parent getRoot() {

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

        VBox chatContainer = new VBox(chatBox, chatInput, sendButton);
        chatContainer.setAlignment(Pos.CENTER);
        chatContainer.setSpacing(10);
        chatContainer.setPrefWidth(300);

        sendButton.setOnAction(e -> {
            System.out.println("TESTING IN CHAT GAMEPLAY");
            String text = chatInput.getText().trim();

            if (!text.isEmpty()) {
                addChatMessage(text);
                System.out.println("TESTING IN CHAT GAMEPLAY  IS EMPTY");
                Message chatMessage = new Message(MessageType.CHAT, text, player1.getUsername(), null);
                client.sendMessage(chatMessage);
                chatInput.clear();  // clear text field
            }
        });



        GridPane boardGrid = createBoardGrid();
        boardGrid.getStyleClass().add("board-grid");

        Text playerOneYou = new Text("Player One (You)");

        HBox player1Title = new HBox(playerOneYou, playerIcon1View);

        String playerTwoName = (player2 != null) ? player2.getUsername() : "Waiting for Opponent";
        Text playerTwo = new Text("Player Two: " + playerTwoName);
        HBox player2Title = new HBox(playerTwo, playerIcon2View);

        HBox playerNames = new HBox(player1Title, player2Title);

        String playerTurn = "Your Turn";
        if (!player1.getIsTurn()) {
            playerTurn = (player2.getUsername() + "'s Turn");
        }
        playerTurnText = new Text(playerTurn);
        VBox boardView = new VBox(playerTurnText, boardGrid, playerNames);
        boardView.getStyleClass().add("board-view");
        boardView.setAlignment(Pos.CENTER);
        boardView.setPrefWidth(580);


        HBox gamePage = new HBox(leftColumn, boardView, chatContainer);

        gamePage.getStyleClass().add("game-page");
        gamePage.setSpacing(20);
        gamePage.setAlignment(Pos.CENTER);

//            ROOT
        StackPane root = new StackPane(gamePage);
        root.setPrefSize(1280, 832);
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;
    }
}



//
//public class GamePlay {
//    int rows = 6;
//    int cols = 6;
//    Circle[][] coinGrid = new Circle[rows][cols];
//    int[][] boardState = new int[rows][cols];
//    private StackPane[][] cellGrid = new StackPane[rows][cols];
//    private GridPane board;
//
//    private Image playerIcon1 = new Image("images/apple.png");
//    private ImageView playerIcon1View = new ImageView(playerIcon1);
//    private Image playerIcon2 = new Image("images/apple.png");
//    private ImageView playerIcon2View = new ImageView(playerIcon2);
//
//    Player player1;
//    Player player2;
//
//    Text playerTurnText = new Text("");
//
//
//    public GamePlay(Player player1, Player player2) {
//        this.player1 = player1;
//        this.player2 = player2;
//        playerIcon1 = player1.getIcon();
//        playerIcon2 = player2.getIcon();
//        playerIcon1View = new ImageView(playerIcon1);
//        playerIcon2View = new ImageView(playerIcon2);
//        board = createBoardGrid();
//    }
//
//    public void updateBoard(int[][] newBoard){
//        player1.setIsTurn(true);
//        System.out.print(player1 + " inside update board");
//        this.boardState = newBoard;
//        redrawBoard();
//    }
//
//

//
//    private GridPane createBoardGrid() {
//        GridPane boardGrid = new GridPane();
//
//        boardGrid.setAlignment(Pos.CENTER);
//        boardGrid.setHgap(10);
//        boardGrid.setVgap(10);
//
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                Circle circle = new Circle(30);
//                circle.setFill(javafx.scene.paint.Color.TRANSPARENT);
//                circle.setStroke(javafx.scene.paint.Color.WHITE);
//                coinGrid[row][col] = circle;
//
//                StackPane cell = new StackPane(circle);
//                cell.setPrefSize(70, 70);
//
//                cellGrid[row][col] = cell;
//
//                int currentCol = col;
//                cell.setOnMouseClicked(e -> handleMove(currentCol));
//
//                boardGrid.add(cell, col, row);
//            }
//        }
//
//        return boardGrid;
//    }
//
//    private void redrawBoard() {
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                cellGrid[row][col].getChildren().clear(); // Clear previous icons
//
//                if (boardState[row][col] == 1) {
//                    ImageView icon = new ImageView(playerIcon1);
//                    icon.setFitHeight(60);
//                    icon.setFitWidth(60);
//                    cellGrid[row][col].getChildren().add(icon);
//                } else if (boardState[row][col] == 2) {
//                    ImageView icon = new ImageView(playerIcon2);
//                    icon.setFitHeight(60);
//                    icon.setFitWidth(60);
//                    cellGrid[row][col].getChildren().add(icon);
//                }
//            }
//        }
//    }
//
//    private void handleMove(int col) {
//        if (!player1.getIsTurn()) {
//            System.out.println("Not your turn!");
//            return;
//        }
//
//        for (int row = rows - 1; row >= 0; row--) {
//            if (boardState[row][col] == 0) {
//                boardState[row][col] = player1.getIsTurn() ? 1 : 2;
//
//                Circle circle = coinGrid[row][col];
//                circle.setVisible(false);
//
//                ImageView icon = new ImageView(playerIcon1);
//                icon.setFitHeight(60);
//                icon.setFitWidth(60);
//                cellGrid[row][col].getChildren().add(icon);
//                player1.setIsTurn(false);
//                if (player2 != null) {
//                    player2.setIsTurn(true);
//                }
//                playerTurnText.setText(player2.getUsername() + "'s Turn");
//                break;
//            }
//        }
//        System.out.println("client board update");
//        player1.sendMessage(new Message("boardUpdate", boardState, player1.getUsername(), null));
//    }
//
//    private void handleOpponentMove(int col) {
//        for (int row = rows - 1; row >= 0; row--) {
//            if (boardState[row][col] == 0) {
//                boardState[row][col] = player1.getIsTurn() ? 2 : 1;
//
//                Circle circle = coinGrid[row][col];
//                circle.setVisible(false);
//
//                ImageView icon = new ImageView(player1.getIsTurn() ? playerIcon1 : playerIcon2);
//                icon.setFitHeight(60);
//                icon.setFitWidth(60);
//                cellGrid[row][col].getChildren().add(icon);
//
//                player1.setIsTurn(true);
//                break;
//            }
//        }
//    }
//}
