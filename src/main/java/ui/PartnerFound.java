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


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class PartnerFound {
    private Image playerIcon;

    public void setPlayerIcon(Image icon){
        this.playerIcon = icon;
    }

    public Parent getRoot(){
        Text found = new Text("Partner Found!\n" + "@other_username");
        Text start = new Text("Starting game now...");

        Image logo = new Image("/images/logo.png", true);
        ImageView logoContainer = new ImageView(logo);
        logoContainer.getStyleClass().add("home-logo");

        VBox findingContainer = new VBox(logoContainer, found, start);
        findingContainer.setAlignment(Pos.CENTER);
        findingContainer.setSpacing(20);

        //        BUTTON

        Button next = new Button("next");
        GamePlay gamePlay = new GamePlay();
        gamePlay.setPlayerIcon(playerIcon);
        next.setOnAction(e-> {
            SceneManager.switchTo(gamePlay.getRoot());
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
