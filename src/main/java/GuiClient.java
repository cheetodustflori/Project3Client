import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import network.Client;
import network.Message;
import ui.Login;
import javafx.event.EventHandler;
import ui.SceneManager;
import javafx.stage.WindowEvent;


import java.util.Scanner;

public class GuiClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(new StackPane(), 1280, 823);
        SceneManager.setStage(primaryStage);
        primaryStage.setScene(scene);

        Client client = new Client();
        client.start();


//        client.sendMessage(new Message("clientUpdate","New player has opened the game"));
        Login login = new Login(client);

        client.setLoginPage(login);

        SceneManager.switchTo(login.getRoot());

//        primaryStage.setScene(new Scene(new TextField("I am not yet implemented")));
//        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("Let's Connect 4 - Client");
        primaryStage.setResizable(false);
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
//
        launch(args);


    }


}
