import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import network.Client;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ui.Login;

public class GuiClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Login login = new Login(primaryStage);
        login.show();

//        primaryStage.setScene(new Scene(new TextField("I am not yet implemented")));
//        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("network.Client");
        primaryStage.setResizable(false);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(832);
        primaryStage.show();

    }

    public static void main(String[] args) {
//        Client clientThread = new Client();
//        clientThread.start();
//        Scanner s = new Scanner(System.in);
//        while (s.hasNext()) {
//            String x = s.next();
//            clientThread.send(x);
//        }

        launch(args);


    }


}
