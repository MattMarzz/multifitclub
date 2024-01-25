package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.controller.GestioneCorsiController;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.time.Instant;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class CoursesController implements Initializable {

    @FXML
    private DatePicker addDateIn;
    @FXML
    private TextField addNameIn;
    @FXML
    private TableView<CorsoBean> coursesTable;
    @FXML
    private TableColumn<CorsoBean, Date> dateCol;
    @FXML
    private DatePicker deleteDateIn;
    @FXML
    private TextField deleteNameIn;
    @FXML
    private TableColumn<CorsoBean, String> nameCol;
    @FXML
    private AnchorPane addCourseView;
    @FXML
    private AnchorPane courseListView;
    @FXML
    private AnchorPane lezioniListView;
    @FXML
    private TableColumn<LezioneBean, String> dayCol;
    @FXML
    private TableColumn<LezioneBean, String> hourCol;
    @FXML
    private TableColumn<LezioneBean, String> salaCol;
    @FXML
    private TableView<LezioneBean> lezioniTable;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCoursesTable();
    }

    public void loadCoursesTable() {
        GestioneCorsiController gestioneCorsiController = new GestioneCorsiController();
        ObservableList<CorsoBean> corsoBeanObservableList = FXCollections.observableArrayList();

        corsoBeanObservableList.addAll(gestioneCorsiController.getAllCorsi());


        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        coursesTable.setItems(corsoBeanObservableList);

        //make rows selectable
        coursesTable.setRowFactory(tv -> {
            TableRow<CorsoBean> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));

            row.setOnMouseClicked(event -> {
                CorsoBean selectedCorso = row.getItem();
                if (selectedCorso != null) {
                    deleteCourseForm(selectedCorso);
                }
            });
            return row;
        });
    }

    public void loadLessonTable() {
        GestioneCorsiController gestioneCorsiController = new GestioneCorsiController();
        ObservableList<LezioneBean> lezioneBeanObservableList = FXCollections.observableArrayList();
        CorsoBean cb = new CorsoBean(deleteNameIn.getText());
        try {
            lezioneBeanObservableList.addAll(gestioneCorsiController.getLezioniByCorsoId(cb));
        } catch (Exception e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
        }

        dayCol.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("ora"));
        salaCol.setCellValueFactory(new PropertyValueFactory<>("sala"));

        lezioniTable.setItems(lezioneBeanObservableList);
    }

    public void deleteCourseForm(CorsoBean corsoBean) {
        deleteNameIn.setText(corsoBean.getName());
        deleteDateIn.setValue(convertToLocalDateViaSqlDate(corsoBean.getStartDate()));
    }

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
    @FXML
    private void onAddCleanBtnClick(ActionEvent event) {
        cleanAddForm();
    }

    @FXML
    private void onAddConfirmBtnClick(ActionEvent event) {
        CorsoBean corsoBean = new CorsoBean();
        corsoBean.setName(addNameIn.getText());
        if(addDateIn.getValue() != null) {
            Instant instant = addDateIn.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            corsoBean.setStartDate(Date.from(instant));
        }
        GestioneCorsiController gestioneCorsiController = new GestioneCorsiController();
        try {
            gestioneCorsiController.insertCourse(corsoBean);
        } catch (Exception e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
        }

        //PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Info", msg);
        loadCoursesTable();
        cleanAddForm();
    }

    @FXML
    private void onDeleteConfirmBtnClick(ActionEvent event) {
        CorsoBean corsoBean = new CorsoBean();
        corsoBean.setName(deleteNameIn.getText());

        Instant instant = deleteDateIn.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        corsoBean.setStartDate(Date.from(instant));

        GestioneCorsiController gestioneCorsiController = new GestioneCorsiController();
        try {
            gestioneCorsiController.removeCorso(corsoBean);
        } catch (Exception e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
        }

        //PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Info", msg);
        loadCoursesTable();
        cleanDeleteForm();
    }

    @FXML
    private void onLezioniBtnClick() {
        if(!deleteNameIn.getText().isBlank()) {
            courseListView.setVisible(false);
            lezioniListView.setVisible(true);
            loadLessonTable();
        }
        //addCourseView.setVisible(false);
    }

    @FXML
    private void onBackToCoursesClick(ActionEvent event) {
        courseListView.setVisible(true);
        lezioniListView.setVisible(false);
        loadCoursesTable();
    }

    public void cleanDeleteForm() {
        deleteDateIn.setValue(null);
        deleteNameIn.setText("");
    }

    public void cleanAddForm() {
        addDateIn.setValue(null);
        addNameIn.setText("");
    }
}
