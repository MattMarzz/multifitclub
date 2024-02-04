package it.uniroma2.dicii.ispw.view.graphicalcontroller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PageHelper {

    public static final String ERROR = "Errore";
    public static final String SUCCESS = "Successo";
    public static final String EMPTY_FIELDS = "Il campo non può essere vuoto.";

    private PageHelper() {}

    public static void changeScene(ActionEvent event, String fxmlFile, String title, UtenteBean utente) throws IOException {
        try {
            URL url = App.class.getResource(fxmlFile);
            if(url == null){
                LoggerManager.logSevere("FXML file non trovato: " + fxmlFile);
                throw new IOException("Errore di sistema. Si prega di riprovare");
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            //pass the information of the logged user to the proper controller
            if(utente != null) {
                AuthenticatedUser controller = loader.getController();
                AuthenticatedUser.setUtenteBean(utente);
                controller.initUserData();
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            LoggerManager.logSevereException("Errore nel loader: " + e.getMessage(), e);
            throw new IOException("Errore non previsto. Si prega di riprovare");
        }
    }


    public static void launchAlert(Alert.AlertType alertType, String title, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static void logout(ActionEvent event) {
        try {
            changeScene(event, "views/login.fxml", "Login", null );
        } catch (IOException e) {
            LoggerManager.logSevereException("Errore nel logout", e);
            Platform.exit();
        }
    }
}
