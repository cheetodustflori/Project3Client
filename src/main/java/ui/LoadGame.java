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
import network.Player;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class LoadGame {

    private Image playerIcon;
    Client client;

    private Player player;

    public LoadGame(Player player, Client client) {
        this.player = player;
        playerIcon = new Image(player.getIcon());
        this.client = client;
    }

    public Parent getRoot(){

        Text finding = new Text("Finding a partner for you...");
        Image loading = new Image("/images/loading.png");
        ImageView loadingContainer = new ImageView(loading);

        Image logo = new Image("/images/logo.png", true);
        ImageView logoContainer = new ImageView(logo);
        logoContainer.getStyleClass().add("home-logo");

        VBox findingContainer = new VBox(logoContainer, finding, loadingContainer);
        findingContainer.setAlignment(Pos.CENTER);
        findingContainer.setSpacing(20);

//        SHOW PLAYER ICON

        if (playerIcon != null) {
            ImageView iconView = new ImageView(playerIcon);
            iconView.setFitHeight(60);
            iconView.setFitWidth(60);
            findingContainer.getChildren().add(iconView);
            findingContainer.setSpacing(20);
        }

        VBox loadingScreen = new VBox(findingContainer);
        loadingScreen.setAlignment(Pos.CENTER);


//        ROOT

        StackPane root = new StackPane(loadingScreen);
        root.setPrefSize(1280, 832);
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;
    }
}
