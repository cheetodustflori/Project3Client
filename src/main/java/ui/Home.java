package ui;

import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
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
import ui.ChooseIcon;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class Home {

    Player player;
    Client client;

    public Home(Player player, Client client) {
        this.player = player;
        this.client = client;
    }


    public Parent getRoot() {

//      LEFT SECTION
        Image logo = new Image("/images/logo.png", true);
        ImageView logoContainer = new ImageView(logo);
        logoContainer.getStyleClass().add("home-logo");

        Text welcome = new Text("Welcome Back,");
        Text username = new Text("@" + player.getUsername());

        welcome.getStyleClass().add("welcome-message");
        username.getStyleClass().add("welcome-username");

        VBox welcomeMessage = new VBox(welcome,username);
        welcomeMessage.setSpacing(5);
        welcomeMessage.getStyleClass().add("welcome-message-container");

        Text wins = new Text("Wins: ");
        Text losses = new Text("Losses: ");
        wins.getStyleClass().add("wins");
        losses.getStyleClass().add("losses");

        Button startGame = new Button("Start New Game");
        startGame.getStyleClass().add("startGame-button");

//        EVENT HANDLER

        startGame.setOnAction(e-> {
            ChooseIcon chooseIcon = new ChooseIcon(player, client);
            String updateMessage = player.getUsername() + " clicked on start game";
            client.sendMessage(new Message(MessageType.TEXT,updateMessage));
            SceneManager.switchTo(chooseIcon.getRoot());
        });

        Button goBack = new Button("return");
        goBack.setOnAction(e-> {
            Login login = new Login(player, client);
            SceneManager.switchTo(login.getRoot());
        });

        VBox welcomeContainer = new VBox(logoContainer, welcomeMessage, wins, losses, startGame, goBack);
        welcomeContainer.setSpacing(15);
        welcomeContainer.setMinWidth(680);
        welcomeContainer.setMaxWidth(680);
        welcomeContainer.getStyleClass().add("welcome-container");
        welcomeContainer.setAlignment(Pos.CENTER);

//        RIGHT SECTION

        Image miniboard = new Image("/images/miniboard.png");
        ImageView miniboardView = new ImageView(miniboard);
        miniboardView.setPreserveRatio(true);


        VBox miniboardContainer = new VBox(miniboardView);
        miniboardContainer.setAlignment(Pos.CENTER);
        miniboardContainer.getStyleClass().add("miniboard-container");
        miniboardContainer.setMouseTransparent(true);

        VBox miniboardMainContainer = new VBox(miniboardContainer);
        miniboardMainContainer.setMinWidth(600);
        miniboardMainContainer.setMaxWidth(600);
        miniboardMainContainer.setAlignment(Pos.CENTER);
        miniboardContainer.getStyleClass().add("miniboard-main-container");



//        HOME SCREEN
        HBox homeHBox = new HBox(welcomeContainer, miniboardMainContainer);

//        ROOT
        StackPane root = new StackPane(homeHBox);
        root.setPrefSize(1280,832);

        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;
    }



}
