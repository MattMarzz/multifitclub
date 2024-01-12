package it.uniroma2.dicii.ispw;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.controllers.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App extends Application {
    public static TypersOfPersistenceLayer persistenceLayer;

    public static void main(String[] args) {
        setPersistenceLayer();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private static void setPersistenceLayer() {
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")){
            Properties properties = new Properties();
            properties.load(input);

            switch(properties.getProperty("persistence.layer")){
                case "JDBC":
                    App.persistenceLayer = TypersOfPersistenceLayer.JDBC;
                    break;
                case "FileSystem":
                    App.persistenceLayer = TypersOfPersistenceLayer.FileSystem;
                    break;
                default:
                    App.persistenceLayer = TypersOfPersistenceLayer.JDBC;
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}