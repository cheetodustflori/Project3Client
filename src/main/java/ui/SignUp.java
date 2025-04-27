package ui;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import network.Client;
import network.Player;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SignUp {

    private Client client;
    private Player player;

    public SignUp(Player player, Client client) {
        this.player = player;
        this.client = client;
    }

    public Parent getRoot() {
        Text usernameText = new Text("create username");
        TextField username = new TextField();
        usernameText.getStyleClass().add("signup-text");
        username.getStyleClass().add("signup-textfield");

        Text passwordText = new Text("create password");
        PasswordField password = new PasswordField();
        passwordText.getStyleClass().add("signup-password-text");
        password.getStyleClass().add("signup-textfield");

        Button signup = new Button("Create Account");
        signup.getStyleClass().add("signup-button");

//        EVENT HANDLER: SIGN UP + SEND PLAYER
        signup.setOnAction(e-> {
            String imgPath = "/images/apple.png";

            String usernameInput = usernameText.toString();

//            Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream, String username, int column, ImageView icon, String gameStatus, Boolean isTurn
            Player newPlayer = new Player(usernameInput, 0, imgPath, "not-started", false);

//            client.send("USERNAME:" + usernameInput);
            client.sendPlayer(newPlayer);
        });

        Text hasAccount = new Text ("Have an account?");
        Button logIn = new Button("Log In Here.");
        logIn.getStyleClass().add("log-in-link");
        VBox hasAccountContainer = new VBox(hasAccount,logIn);
        hasAccountContainer.setSpacing(5);
        hasAccountContainer.setAlignment(Pos.CENTER);

//        EVENT HANDLER: NAV TO LOG IN

        logIn.setOnAction( e -> {
            Login login = new Login(player, client);
            SceneManager.switchTo(login.getRoot());
        });

        Image title = new Image("/images/logo.png", true);
        ImageView titleContainer = new ImageView(title);

        TranslateTransition bounce = new TranslateTransition(Duration.seconds(1), titleContainer);
        bounce.setByY(-20);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(TranslateTransition.INDEFINITE);
        bounce.setDelay(Duration.seconds(0.5));
        bounce.play();

        AnchorPane imageLayer = new AnchorPane();
        imageLayer.setMouseTransparent(true);

        Image picnicblanket = new Image("/images/picnicblanket.png", true);
        ImageView picnicContainer = new ImageView(picnicblanket);
        picnicContainer.getStyleClass().add("picnic-container");
        picnicContainer.setPreserveRatio(true);
//        AnchorPane picnicAnchor = new AnchorPane(picnicContainer);
        AnchorPane.setTopAnchor(picnicContainer, -15.0);
        AnchorPane.setRightAnchor(picnicContainer, -15.0);

        Image fruitbasket = new Image("/images/fruitbasket.png", true);
        ImageView basketContainer = new ImageView(fruitbasket);
        basketContainer.getStyleClass().add("basket-container");
        basketContainer.setPreserveRatio(true);
//        AnchorPane basketAnchor = new AnchorPane(basketContainer);
        AnchorPane.setLeftAnchor(basketContainer, 10.0);
        AnchorPane.setBottomAnchor(basketContainer, 10.0);

        VBox usernameContainer = new VBox(usernameText, username);
        usernameContainer.setAlignment(Pos.CENTER);
        usernameContainer.setSpacing(5);
        VBox passwordContainer = new VBox(passwordText, password);
        passwordContainer.setAlignment(Pos.CENTER);
        passwordContainer.setSpacing(5);

        VBox loginContainer = new VBox(titleContainer, usernameContainer,passwordContainer,signup,hasAccountContainer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(20);

        imageLayer.getChildren().addAll(picnicContainer, basketContainer);


        StackPane root = new StackPane(loginContainer, imageLayer);
        root.setPrefSize(1280,832);

//        Scene scene = new Scene(root,1280,832);
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
//        stage.setScene(scene);
//        stage.setTitle("Log In");
//        stage.show();

        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return root;

    }

}
