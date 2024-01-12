package it.uniroma2.dicii.ispw;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.controllers.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exceptions.InvalidDataException;
import it.uniroma2.dicii.ispw.models.utente.Utente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App extends Application {
    private static TypersOfPersistenceLayer persistenceLayer;

    public static TypersOfPersistenceLayer getPersistenceLayer(){
        return persistenceLayer;
    }

    public static void main(String[] args) {
        setPersistenceLayer();
//        UtenteBean u = new UtenteBean("Luca", "ad", "BNCMRA70A20H501B", "10/10/2010", "em@em.it", "abc", Ruolo.UTENTE );
//        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
//        try {
//            gestioneUtentiController.insertUtente(u);
//        } catch (InvalidDataException e) {
//            throw new RuntimeException(e);
//        }
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
                    App.persistenceLayer = TypersOfPersistenceLayer.FILE_SYSTEM;
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