package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.controller.GestioneCorsiController;
import it.uniroma2.dicii.ispw.controller.ProgrammazioneController;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchedulingController implements Initializable {

    @FXML
    private TableColumn<LezioneBean, String> courseCol;
    @FXML
    private ComboBox<String> courseIn;
    @FXML
    private TableColumn<LezioneBean, String> dayCol;
    @FXML
    private ListView<String> daysList;
    @FXML
    private TableColumn<LezioneBean, Time> hourCol;
    @FXML
    private TableColumn<LezioneBean, String> istruCol;
    @FXML
    private TableView<LezioneBean> lessonsTable;
    @FXML
    private TableColumn<LezioneBean, String> salaCol;
    @FXML
    private TextField salaIn;
    @FXML
    private TextField timePicker;
    @FXML
    private Label info;
    @FXML
    private Label hourErr;


    @FXML
    void onAddLessonBtnClick(ActionEvent event) {
        ObservableList<String> selectedItems = daysList.getSelectionModel().getSelectedItems();

        if(courseIn.getValue() == null || salaIn.getText().isBlank() || timePicker.getText().isBlank() || selectedItems.isEmpty() )
            info.setText("Campi mancanti");
        else {

            String tmRegex = "\\d{2}:\\d{2}";
            Pattern pattern = Pattern.compile(tmRegex);
            Matcher matcher = pattern.matcher(timePicker.getText());
            if (!matcher.matches()) {
                hourErr.setText("Formato data errato");

            } else {

                Time oraInizio = Time.valueOf(timePicker.getText() + ":00");

                LezioneBean lb = new LezioneBean();
                List<String> dayList = new ArrayList<>(selectedItems);

                lb.setOra(oraInizio);
                lb.setGiornoList(dayList);
                lb.setSala(salaIn.getText());
                lb.setCorso(courseIn.getValue());

                try {
                    new ProgrammazioneController().insertLezioni(lb, AuthenticatedUser.getUtenteBean());
                } catch (ItemNotFoundException | InvalidDataException | ItemAlreadyExistsException e) {
                    PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
                } finally {
                    clearAll();
                }
            }
        }

    }

    private void clearAll() {
        loadPrgrammation();
        loadCourses();
        hourErr.setText("");
        info.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCourses();
        loadPrgrammation();
        daysList.getItems().addAll("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato");
        daysList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadCourses() {
        List<CorsoBean> corsoBeanList = new GestioneCorsiController().getAllCorsi();
        courseIn.getItems().clear();

        for (CorsoBean cb: corsoBeanList) {
            courseIn.getItems().add(cb.getName());
        }
    }

    private void loadPrgrammation() {
        List<LezioneBean> lezioneBeanList = new ProgrammazioneController().getAllLezioni();
        ObservableList<LezioneBean> lezioneBeanObservableList = FXCollections.observableArrayList();
        lezioneBeanObservableList.addAll(lezioneBeanList);

        courseCol.setCellValueFactory(new PropertyValueFactory<>("corso"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("ora"));
        istruCol.setCellValueFactory(new PropertyValueFactory<>("cf"));
        salaCol.setCellValueFactory(new PropertyValueFactory<>("sala"));

        lessonsTable.setItems(lezioneBeanObservableList);
    }

}
