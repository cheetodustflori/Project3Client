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

public class Home {
//    private final Stage stage;

    public Home() {
//        this.stage = primaryStage;
    }

    public Parent getRoot() {
        Image title = new Image("/images/logo.png", true);
        ImageView titleContainer = new ImageView(title);
//        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return new StackPane(new Text("Welcome to home screen"));
    }



}
