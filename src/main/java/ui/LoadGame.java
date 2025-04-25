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


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class LoadGame {

    private Image playerIcon;

    private Client client;

    public LoadGame(Client client) {
        this.client = client;
    }

    public void setPlayerIcon(Image icon){
        this.playerIcon = icon;
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


        //   EVENT HANDLER: NEXT PAGE

        Button next = new Button("next");
        PartnerFound partnerFound = new PartnerFound(client);
        partnerFound.setPlayerIcon(playerIcon);
        next.setOnAction(e-> {
            SceneManager.switchTo(partnerFound.getRoot());
        });


        VBox loadingScreen = new VBox(findingContainer, next);
        loadingScreen.setAlignment(Pos.CENTER);


//        ROOT

        StackPane root = new StackPane(loadingScreen);
        root.setPrefSize(1280, 832);
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;
    }
}
