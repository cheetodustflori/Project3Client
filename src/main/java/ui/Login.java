package ui;

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



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Login {

    private final Stage stage;

    public Login(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void show() {
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

        Text noAccount = new Text ("Don't have an account?");
        Button createAccount = new Button("Create Account Here.");
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

        VBox loginContainer = new VBox(titleContainer, usernameContainer,passwordContainer,login,noAccountContainer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(20);

//        // Optional: Resize image
//        imageView.setFitWidth(75);
//        imageView.setFitHeight(75);
//        imageView.setPreserveRatio(true);

        imageLayer.getChildren().addAll(picnicContainer, basketContainer);


        StackPane root = new StackPane(loginContainer, imageLayer);
        root.setPrefSize(1280,832);

        Scene scene = new Scene(root,1280,832);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.show();

    }

}
