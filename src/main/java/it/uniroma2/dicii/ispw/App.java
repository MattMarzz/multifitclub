package it.uniroma2.dicii.ispw;

import atlantafx.base.theme.PrimerDark;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App extends Application {
    private static TypesOfPersistenceLayer persistenceLayer;

    public static TypesOfPersistenceLayer getPersistenceLayer(){
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
//        AnnouncementController controller = new AnnouncementController();
//        AnnouncementBean ab = new AnnouncementBean();
//        ab.setTitle("Annuncio");
//        ab.setText("bellissimo testo");
//        ab.setDate(Timestamp.valueOf(LocalDateTime.now()));
//        ab.setSender("BNCMRA70A20H501B");
//        try {
//            controller.publishNewAnnouncement(ab);
//        } catch (InvalidDataException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/login.fxml"));
        InputStream url = getClass().getResourceAsStream("/images/icon.png");

        if(url != null) {
            Image icon = new Image(url);
            stage.getIcons().add(icon);
        }

        stage.initStyle(StageStyle.UNIFIED);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MultiFitClub");
        stage.setScene(scene);
        stage.show();
    }

    private static void setPersistenceLayer() {
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")){
            Properties properties = new Properties();
            properties.load(input);

            switch(properties.getProperty("persistence.layer")){
                case "JDBC":
                    App.persistenceLayer = TypesOfPersistenceLayer.JDBC;
                    break;
                case "FileSystem":
                    App.persistenceLayer = TypesOfPersistenceLayer.FILE_SYSTEM;
                    break;
                default:
                    App.persistenceLayer = TypesOfPersistenceLayer.JDBC;
                    break;
            }
        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile leggere il layer di persistenza dal file di configurazione.\n" +
                    "Si procede con la scelta di default: JDBC", e);
            App.persistenceLayer = TypesOfPersistenceLayer.JDBC;
        }
    }
}