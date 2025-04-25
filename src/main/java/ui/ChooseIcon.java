package ui;

import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;
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
public class ChooseIcon {
    private Image selectedIcon;
    private Client client;

    public ChooseIcon(Client client) {
        this.client = client;
    }

    public Parent getRoot(){

        Text warningText = new Text("");
        warningText.getStyleClass().add("warning-text"); // optional CSS class


//        SETTING UP ICONS IMAGEVIEW
        Image apple = new Image("/images/apple.png", true);
        ImageView appleView = new ImageView(apple);
        appleView.setFitHeight(100);
        appleView.setFitWidth(100);
        StackPane applePane = new StackPane(appleView);

        Image mango = new Image("/images/mango.png", true);
        ImageView mangoView = new ImageView(mango);
        mangoView.setFitHeight(100);
        mangoView.setFitWidth(100);
        StackPane mangoPane = new StackPane(mangoView);

        Image lemon = new Image("/images/lemon.png", true);
        ImageView lemonView = new ImageView(lemon);
        lemonView.setFitHeight(100);
        lemonView.setFitWidth(100);
        StackPane lemonPane = new StackPane(lemonView);

        Image blueberry = new Image("/images/blueberry.png", true);
        ImageView blueberryView = new ImageView(blueberry);
        blueberryView.setFitHeight(100);
        blueberryView.setFitWidth(100);
        StackPane blueberryPane = new StackPane(blueberryView);

        Image orange = new Image("/images/orange.png", true);
        ImageView orangeView = new ImageView(orange);
        orangeView.setFitHeight(100);
        orangeView.setFitWidth(100);
        StackPane orangePane = new StackPane(orangeView);

        Image strawberry = new Image("/images/strawberry.png", true);
        ImageView strawberryView = new ImageView(strawberry);
        strawberryView.setFitHeight(100);
        strawberryView.setFitWidth(100);
        StackPane strawberryPane = new StackPane(strawberryView);

        Image dragonfruit = new Image("/images/dragonfruit.png", true);
        ImageView dragonfruitView = new ImageView(dragonfruit);
        dragonfruitView.setFitHeight(100);
        dragonfruitView.setFitWidth(100);
        StackPane dragonfruitPane = new StackPane(dragonfruitView);

        Image kiwi = new Image("/images/kiwi.png", true);
        ImageView kiwiView = new ImageView(kiwi);
        kiwiView.setFitHeight(100);
        kiwiView.setFitWidth(100);
        StackPane kiwiPane = new StackPane(kiwiView);

        Image starfruit = new Image("/images/starfruit.png", true);
        ImageView starfruitView = new ImageView(starfruit);
        starfruitView.setFitHeight(100);
        starfruitView.setFitWidth(100);
        StackPane starfruitPane = new StackPane(starfruitView);

// EVENT HANDLING LIST
        ArrayList<StackPane> iconPanes = new ArrayList<>();
        iconPanes.add(applePane);
        iconPanes.add(mangoPane);
        iconPanes.add(lemonPane);
        iconPanes.add(blueberryPane);
        iconPanes.add(orangePane);
        iconPanes.add(strawberryPane);
        iconPanes.add(dragonfruitPane);
        iconPanes.add(kiwiPane);
        iconPanes.add(starfruitPane);

// EVENT HANDLER: HIGHLIGHT ICON
        for (int i = 0; i < iconPanes.size(); i++) {
            final int index = i;
            StackPane pane = iconPanes.get(i);
            pane.setOnMouseClicked(event -> {
                for (StackPane other : iconPanes) {
                    other.getStyleClass().remove("selected-icon");
                }

                pane.getStyleClass().add("selected-icon");

                switch (index) {
                    case 0 -> selectedIcon = apple;
                    case 1 -> selectedIcon = mango;
                    case 2 -> selectedIcon = lemon;
                    case 3 -> selectedIcon = blueberry;
                    case 4 -> selectedIcon = orange;
                    case 5 -> selectedIcon = strawberry;
                    case 6 -> selectedIcon = dragonfruit;
                    case 7 -> selectedIcon = kiwi;
                    case 8 -> selectedIcon = starfruit;
                }
            });
        }


        //      LEFT SECTION
        Image logo = new Image("/images/logo.png", true);
        ImageView logoContainer = new ImageView(logo);
        logoContainer.getStyleClass().add("home-logo");

        Text chooseMessage = new Text("Choose Your Icon!");
        Text subtitle = new Text("Choose an icon for your coin!");

        chooseMessage.getStyleClass().add("choose-message");

        VBox messageContainer = new VBox(chooseMessage,subtitle);
        messageContainer.setSpacing(5);
        messageContainer.getStyleClass().add("choose-message-container");


//        EVENT HANDLER: DONE AND RETURN

        Button done = new Button("Done");
        done.getStyleClass().add("done-button");

        Button goBack = new Button("return");
        Home home = new Home(client);
        goBack.setOnAction(e-> {
            SceneManager.switchTo(home.getRoot());
        });

        VBox welcomeContainer = new VBox(logoContainer, messageContainer,done, warningText, goBack);
        welcomeContainer.setSpacing(30);
        welcomeContainer.setMinWidth(680);
        welcomeContainer.setMaxWidth(680);
        welcomeContainer.getStyleClass().add("welcome-container");
        welcomeContainer.setAlignment(Pos.CENTER);

//        RIGHT SCREEN -- CHOOSE ICON

        GridPane iconGrid = new GridPane();
        iconGrid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
        iconGrid.getColumnConstraints().add(new ColumnConstraints(100));
        iconGrid.getColumnConstraints().add(new ColumnConstraints(100));

        GridPane.setConstraints(applePane, 0, 0);
        GridPane.setConstraints(mangoPane, 1, 0);
        GridPane.setConstraints(lemonPane, 2, 0);
        GridPane.setConstraints(blueberryPane, 0, 1);
        GridPane.setConstraints(orangePane, 1, 1);
        GridPane.setConstraints(strawberryPane, 2, 1);
        GridPane.setConstraints(dragonfruitPane, 0, 2);
        GridPane.setConstraints(kiwiPane, 1, 2);
        GridPane.setConstraints(starfruitPane, 2, 2);

        iconGrid.getChildren().addAll(
                applePane, mangoPane, lemonPane,
                blueberryPane, orangePane, strawberryPane,
                dragonfruitPane, kiwiPane, starfruitPane
        );

        iconGrid.setAlignment(Pos.CENTER);
        iconGrid.getStyleClass().add("icon-grid");

        VBox iconView = new VBox(iconGrid);
        iconView.setAlignment(Pos.CENTER);

        //        EVENT HANDLER: PASS IMAGE

        done.setOnAction(e-> {
            if (selectedIcon != null) {
                LoadGame loadGame = new LoadGame(client);
                loadGame.setPlayerIcon(selectedIcon);
                SceneManager.switchTo(loadGame.getRoot());
            } else {
                warningText.setText("Please choose an icon first");
            }
        });

//      HOME SCREEN

        HBox chooseIcon = new HBox(welcomeContainer, iconView);

//        ROOT
        StackPane root = new StackPane(chooseIcon);
        root.setPrefSize(1280,832);

        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;
    }
}
