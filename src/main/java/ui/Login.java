package ui;

import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import network.Client;
import network.Message;
import network.MessageType;
import network.Player;


public class Login {

    private Client client;
    private Player player;


    public Login(Client client) {
        this.client = client;
    }

    public Login(Player player, Client client) {
        this.player = player;
        this.client = client;

    }

    private Label errorLabel = new Label("");

    public void showError(String errorMsg){
        Platform.runLater(() -> {
            errorLabel.setText(errorMsg);
            errorLabel.setStyle("-fx-text-fill: red");
        });
    }

    public Parent getRoot() {

        Text warningText = new Text("");
        warningText.getStyleClass().add("warning-text");
        warningText.setText("");

        Text usernameText = new Text("username");
        TextField username = new TextField();
        usernameText.getStyleClass().add("login-text");
        username.getStyleClass().add("login-textfield");


        Text passwordText = new Text("password");
        PasswordField password = new PasswordField();
        passwordText.getStyleClass().add("password-text");
        password.getStyleClass().add("login-textfield");

        Button login = new Button("Log In");
        login.getStyleClass().add("login-button");

//        EVENT HANDLER: LOG IN AND CREATE PLAYER
        login.setOnAction(e -> {
            String usernameInput = username.getText().trim();
            if (usernameInput.isEmpty()) {
                usernameInput = "Guest" + client.getClientCount();
            }

            String imgPath = "/images/apple.png";

//          String username, int column, ImageView icon, String gameStatus, Boolean isTurn
            Player newPlayer = new Player(usernameInput, 0, imgPath, "not-started", false);
            client.setPlayer(newPlayer);
            String updateMessage = usernameInput + " has logged in";
            client.sendMessage(new Message(MessageType.USERNAMECHECK,updateMessage));

        });

        Text noAccount = new Text ("Don't have an account?");
        Button createAccount = new Button("Create Account Here.");

        createAccount.setOnAction( e -> {
            SignUp signup = new SignUp(player, client);
            String updateMessage = "User navigated to create account";
            client.sendMessage(new Message(MessageType.TEXT,updateMessage));
            SceneManager.switchTo(signup.getRoot());

        });

        createAccount.getStyleClass().add("create-account-link");
        VBox noAccountContainer = new VBox(noAccount,createAccount);
        noAccountContainer.setSpacing(5);
        noAccountContainer.setAlignment(Pos.CENTER);

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

        VBox loginContainer = new VBox(titleContainer, errorLabel, usernameContainer,login,noAccountContainer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(20);

//        IMAGE LAYER

        imageLayer.getChildren().addAll(picnicContainer, basketContainer);

//        ROOT
        StackPane root = new StackPane(loginContainer, imageLayer);
        root.setPrefSize(1280,832);

        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return root;

    }

}
