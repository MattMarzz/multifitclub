package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.GestioneCorsiController;
import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UserListController implements Initializable {
    @FXML
    private ListView<CorsoBean> enrollmentList;
    @FXML
    private AnchorPane usersTablePane;
    @FXML
    private AnchorPane enrollmentView;
    @FXML
    private AnchorPane teachingView;
    @FXML
    private ListView<CorsoBean> teachingList;
    @FXML
    private Button saveBtn;
    @FXML
    private Button teachingBtn;
    @FXML
    private TextField cfIn;
    @FXML
    private TextField nameIn;
    @FXML
    private TextField surnameIn;
    @FXML
    private DatePicker dateIn;
    @FXML
    private TextField emailIn;
    @FXML
    private ComboBox<Ruolo> roleIn;
    @FXML
    private Button enrollmentBtn;
    @FXML
    private TableColumn<UtenteBean,String> cfCol;
    @FXML
    private TableColumn<UtenteBean,String> nameCol;
    @FXML
    private TableColumn<UtenteBean,String> dataCol;
    @FXML
    private TableColumn<UtenteBean,String> surnameCol;
    @FXML
    private TableColumn<UtenteBean,String> emailCol;
    @FXML
    private TableColumn<UtenteBean, Ruolo> ruoloCol;
    @FXML
    private TableView<UtenteBean> usersTable;
    @FXML
    private ListView<CorsoBean> addEnrollmentList;
    @FXML
    private Label deleteEnrollmentInfo;
    @FXML
    private Label addEnrollmentInfo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUsersListTable();
        usersTable.setVisible(true);
        enrollmentView.setVisible(false);
        teachingView.setVisible(false);
    }


    private ObservableList<CorsoBean> corsoBeanObservableListEnroll;
    public void loadEnrollmentsList(UtenteBean utenteBean) {
        deleteEnrollmentInfo.setText("");
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        corsoBeanObservableListEnroll = FXCollections.observableArrayList();
        try {
            corsoBeanObservableListEnroll.addAll(gestioneUtentiController.getEnrollmentsByUtente(utenteBean));
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, "Impossibile trovare le iscrizione dell'utente " + utenteBean.getName());
        }

        enrollmentList.setCellFactory(param -> new ListCell<CorsoBean>() {
            @Override
            protected void updateItem(CorsoBean corsoBean, boolean empty) {
                super.updateItem(corsoBean, empty);

                if (empty || corsoBean == null) {
                    setText(null);
                } else {
                    setText(corsoBean.getName());
                }
            }
        });
        enrollmentList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        enrollmentList.setCursor(Cursor.HAND);
        enrollmentList.setItems(corsoBeanObservableListEnroll);

        enrollmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDeleteEnrollmentInfoLbl(newSelection);
            }
        });
    }

    public void loadAvailableCourse(UtenteBean utenteBean) {
        addEnrollmentInfo.setText("");
        GestioneCorsiController gestioneCorsiController = new GestioneCorsiController();
        ObservableList<CorsoBean> corsoBeanObservableList = FXCollections.observableArrayList();
        List<CorsoBean> allCorsi = gestioneCorsiController.getAllCorsi();

        List<CorsoBean> corsiNonInscritti = allCorsi.stream()
                .filter(corso -> corsoBeanObservableListEnroll.stream().noneMatch(iscritto -> iscritto.getName().equals(corso.getName())))
                .toList();

        corsoBeanObservableList.addAll(corsiNonInscritti);

        addEnrollmentList.setCellFactory(param -> new ListCell<CorsoBean>() {
            @Override
            protected void updateItem(CorsoBean corsoBean, boolean empty) {
                super.updateItem(corsoBean, empty);

                if (empty || corsoBean == null) {
                    setText(null);
                } else {
                    setText(corsoBean.getName());
                }
            }
        });

        addEnrollmentList.setItems(corsoBeanObservableList);

        addEnrollmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            addEnrollmentList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            addEnrollmentList.setCursor(Cursor.HAND);
            if (newSelection != null) {
                showAddEnrollmentInfoLbl(newSelection);
            }
        });
    }

    public void loadTeachingList(UtenteBean utenteBean) {
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        ObservableList<CorsoBean> corsoBeanObservableList = FXCollections.observableArrayList();
        try {
            corsoBeanObservableList.addAll(gestioneUtentiController.getTeachingByUtente(utenteBean));
        } catch (ItemNotFoundException e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR,  e.getMessage());
        }

        teachingList.setCellFactory(param -> new ListCell<CorsoBean>() {
            @Override
            protected void updateItem(CorsoBean corsoBean, boolean empty) {
                super.updateItem(corsoBean, empty);

                if (empty || corsoBean == null) {
                    setText(null);
                } else {
                    setText(corsoBean.getName());
                }
            }
        });
        teachingList.setItems(corsoBeanObservableList);
    }


    public void showDeleteEnrollmentInfoLbl(CorsoBean corsoBean) {
        deleteEnrollmentInfo.setText(corsoBean.getName());
    }

    public void showAddEnrollmentInfoLbl(CorsoBean corsoBean) {
        addEnrollmentInfo.setText(corsoBean.getName());
    }

    @FXML
    private void onDeleteEnrollment() {
        CorsoBean corsoBean = new CorsoBean();
        UtenteBean utenteBean = new UtenteBean();
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        corsoBean.setName(deleteEnrollmentInfo.getText());
        utenteBean.setCf(cfIn.getText());
        try {
            gestioneUtentiController.removeEnrollmentByUtente(utenteBean, corsoBean);
        } catch (ItemNotFoundException e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
        }
        loadEnrollmentsList(utenteBean);
        loadAvailableCourse(utenteBean);
    }

    @FXML
    private void onAddEnrollment() {
        CorsoBean corsoBean = new CorsoBean();
        UtenteBean utenteBean = new UtenteBean();
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        corsoBean.setName(addEnrollmentInfo.getText());
        utenteBean.setCf(cfIn.getText());
        try {
            gestioneUtentiController.addEnrollmentToUtente(utenteBean, corsoBean);
        } catch (Exception e) {
            LoggerManager.logSevereException("Errore in fase di registrazione dell'iscrizione", e);
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, "Inserimento non riuscito!");
        }
        loadEnrollmentsList(utenteBean);
        loadAvailableCourse(utenteBean);
    }
    public void loadUsersListTable(){
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        ObservableList<UtenteBean> utenteBeanObservableList = FXCollections.observableArrayList();
        utenteBeanObservableList.addAll(gestioneUtentiController.getAllUtenti());

        cfCol.setCellValueFactory(new PropertyValueFactory<>("cf"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        ruoloCol.setCellValueFactory(new PropertyValueFactory<>("ruolo"));

        usersTable.setItems(utenteBeanObservableList);

        //make rows selectable
        usersTable.setRowFactory(tv -> {
            TableRow<UtenteBean> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));

            row.setOnMouseClicked(event -> {
                UtenteBean selectedUtente = row.getItem();
                if (selectedUtente != null) {
                    editInfoForm(selectedUtente);
                }
            });

            return row;
        });
    }

    public void editInfoForm(UtenteBean utenteBean) {
        cfIn.setText(utenteBean.getCf());
        nameIn.setText(utenteBean.getName());
        surnameIn.setText(utenteBean.getSurname());
        emailIn.setText(utenteBean.getEmail());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date  = sdf.parse(utenteBean.getBirthDate());
        } catch (ParseException e) {
            LoggerManager.logSevereException("Formato data non valida " + e.getMessage());
        }
        if(date != null)
            dateIn.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        //check if combo is already loaded
        if(roleIn.getItems().isEmpty()) {
            Ruolo[] ruoli = Ruolo.values();
            roleIn.getItems().addAll(ruoli);
        }

        roleIn.setValue(utenteBean.getRuolo());

        switch(utenteBean.getRuolo()) {
            case SEGRETERIA -> {
                enrollmentBtn.setDisable(true);
                teachingBtn.setDisable(true);
                teachingBtn.setCursor(Cursor.NONE);
                enrollmentBtn.setCursor(Cursor.NONE);
            }
            case ISTRUTTORE -> {
                enrollmentBtn.setDisable(true);
                teachingBtn.setDisable(false);
                teachingBtn.setCursor(Cursor.HAND);
                enrollmentBtn.setCursor(Cursor.NONE);
            }
            case UTENTE -> {
                teachingBtn.setDisable(true);
                enrollmentBtn.setDisable(false);
                enrollmentBtn.setCursor(Cursor.HAND);
                teachingBtn.setCursor(Cursor.NONE);
            }
        }

    }

    @FXML
    private void onSaveBtnClick() {
        UtenteBean utenteBean = new UtenteBean();
        utenteBean.setCf(cfIn.getText());
        utenteBean.setName(nameIn.getText());
        utenteBean.setSurname(surnameIn.getText());
        utenteBean.setEmail(emailIn.getText());
        utenteBean.setRuolo(roleIn.getValue());
        utenteBean.setBirthDate(dateIn.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        String esito = "";
        try {
            esito = gestioneUtentiController.updateUtente(utenteBean);
        } catch (Exception e) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
        }

        if(!esito.isEmpty()) {
            PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Modifica effettuata", esito);
            loadUsersListTable();
        }

    }

    @FXML
    private void onEnrollmentBtnClick() {
        usersTablePane.setVisible(false);
        enrollmentView.setVisible(true);
        teachingView.setVisible(false);
        UtenteBean ub = new UtenteBean();
        ub.setCf(cfIn.getText());
        loadEnrollmentsList(ub);
        loadAvailableCourse(ub);
    }

    @FXML
    private void onTeachingBtnClick() {
        usersTablePane.setVisible(false);
        enrollmentView.setVisible(false);
        teachingView.setVisible(true);
        UtenteBean ub = new UtenteBean();
        ub.setCf(cfIn.getText());
        loadTeachingList(ub);
    }

    @FXML
    private void onBackToUsersClick() {
        usersTablePane.setVisible(true);
        enrollmentView.setVisible(false);
        teachingView.setVisible(false);
        loadUsersListTable();
    }

}