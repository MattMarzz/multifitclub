package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Text label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText("Dashboard work in progress...");
    }
}
