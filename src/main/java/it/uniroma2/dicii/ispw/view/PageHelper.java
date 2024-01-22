package it.uniroma2.dicii.ispw.view;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PageHelper {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, UtenteBean utente, AuthenticatedUser controller) throws IOException {
        try {
            URL url = App.class.getResource(fxmlFile);
            if(url == null){
                LoggerManager.logSevereException("FXML file non trovato: " + fxmlFile);
                throw new IOException("Errore di sistema. Si prega di riprovare");
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            //pass the information of the logged user to the proper controller
            controller = loader.getController();
            controller.setUtenteBean(utente);
            controller.initUserData();

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
}
