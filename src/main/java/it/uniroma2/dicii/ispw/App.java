package it.uniroma2.dicii.ispw;

import atlantafx.base.theme.PrimerDark;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.TypesOfUIs;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.CliController;
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
    private static TypesOfUIs ui;

    public static TypesOfPersistenceLayer getPersistenceLayer(){
        return persistenceLayer;
    }

    public static void main(String[] args) {
        setPersistenceLayerAndUi();
        if(App.ui.equals(TypesOfUIs.JAVAFX))
            launch();
        else
            new CliController().start();
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

    private static void setPersistenceLayerAndUi() {
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")){
            Properties properties = new Properties();
            properties.load(input);

            //persistence layer
            if (properties.getProperty("persistence.layer").equals("FileSystem")) {
                App.persistenceLayer = TypesOfPersistenceLayer.FILE_SYSTEM;
            } else {
                App.persistenceLayer = TypesOfPersistenceLayer.JDBC;
            }

            //user interface
            if(properties.getProperty("ui").equals("javafx")) {
                App.ui = TypesOfUIs.JAVAFX;
            } else {
                App.ui = TypesOfUIs.CLI;
            }

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile leggere dal file di configurazione.\n" +
                    "Si procede con la scelta di default: JDBC-JavaFx", e);
            App.persistenceLayer = TypesOfPersistenceLayer.JDBC;
            App.ui = TypesOfUIs.JAVAFX;
        }
    }
}