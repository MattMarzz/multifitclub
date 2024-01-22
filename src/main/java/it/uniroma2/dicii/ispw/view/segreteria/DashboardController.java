package it.uniroma2.dicii.ispw.view.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.GestioneCorsiController;
import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.AuthenticatedUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DashboardController extends AuthenticatedUser {
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
    private Button coursesListBtn;
    @FXML
    private Button addCourseBtn;
    @FXML
    private AnchorPane addCourseView;
    @FXML
    private Button addUserBtn;
    @FXML
    private AnchorPane addUserView;
    @FXML
    private Button announceBtn;
    @FXML
    private AnchorPane announceView;
    @FXML
    private AnchorPane coursesListView;
    @FXML
    private Button dashboardBtn;
    @FXML
    private AnchorPane dashboardView;
    @FXML
    private Label nameLbl;
    @FXML
    private Button reservationBtn;
    @FXML
    private AnchorPane reservationView;
    @FXML
    private Button schedulingBtn;
    @FXML
    private AnchorPane schedulingView;
    @FXML
    private Button userListBtn;
    @FXML
    private AnchorPane usersListView;
    @FXML
    private ListView<CorsoBean> addEnrollmentList;
    @FXML
    private Label deleteEnrollmentInfo;
    @FXML
    private Label addEnrollmentInfo;

    @Override
    public void initUserData() {
        nameLbl.setText(this.utenteBean.getName());
    }

    public void switchView(ActionEvent event) {

        if(event.getSource() == dashboardBtn) {
            setRightView(true, false, false, false, false, false, false, false);
        } else if (event.getSource() == reservationBtn) {
            setRightView(false, true, false, false, false, false, false, false);
        } else if (event.getSource() == announceBtn) {
            setRightView(false, false, true, false, false, false, false, false);
        } else if (event.getSource() == userListBtn) {
            setRightView(false, false, false, true, false, false, false, false);
            loadUsersListTable();
        } else if (event.getSource() == addUserBtn) {
            setRightView(false, false, false, false, true, false, false, false);
        } else if (event.getSource() == coursesListBtn) {
            setRightView(false, false, false, false, false, true, false, false);
        } else if (event.getSource() == addCourseBtn) {
            setRightView(false, false, false, false, false, false, true, false);
        } else if (event.getSource() == schedulingBtn) {
            setRightView(false, false, false, false, false, false, false, true);
        }
    }

    private void setRightView(boolean dash, boolean res, boolean ann, boolean u, boolean addu, boolean c, boolean addc, boolean sch) {
        dashboardView.setVisible(dash);
        reservationView.setVisible(res);
        announceView.setVisible(ann);
        usersListView.setVisible(u);
        addUserView.setVisible(addu);
        coursesListView.setVisible(c);
        addCourseView.setVisible(addc);
        schedulingView.setVisible(sch);
    }

    public ObservableList<CorsoBean> corsoBeanObservableListEnroll;
    public void loadEnrollmentsList(UtenteBean utenteBean) {
        deleteEnrollmentInfo.setText("");
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        corsoBeanObservableListEnroll = FXCollections.observableArrayList();
        try {
            corsoBeanObservableListEnroll.addAll(gestioneUtentiController.getEnrollmentsByUtente(utenteBean));
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            launchAlert(Alert.AlertType.ERROR, "Errore", "Impossibile trovare le iscrizione dell'utente " + utenteBean.getName());
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
        List<CorsoBean> allCorsi = new ArrayList<>();
        try {
            allCorsi = gestioneCorsiController.getAllCorsi();
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            launchAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare la lista dei corsi");
        }

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
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            launchAlert(Alert.AlertType.ERROR, "Errore", "Impossibile trovare gli insegnamenti dell'utente " + utenteBean.getName());
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
        } catch (Exception e) {
            LoggerManager.logSevereException("Errore nell'eliminazione dell'iscrizione", e);
            launchAlert(Alert.AlertType.ERROR, "Errore", "Eliminazione non riuscita!");
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
            launchAlert(Alert.AlertType.ERROR, "Errore", "Inserimento non riuscito!");
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
            LoggerManager.logSevereException("Formato data non valida" + e.getMessage());
        }
        if(date != null)
            dateIn.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        Ruolo[] ruoli = Ruolo.values();
        roleIn.getItems().addAll(ruoli);
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
            esito = gestioneUtentiController.editUtente(utenteBean);
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            launchAlert(Alert.AlertType.ERROR, "Errore", "Errore in fase di aggiornamento!");
        }

        if(!esito.isEmpty()) {
            launchAlert(Alert.AlertType.INFORMATION, "Modifica effettuata", esito);
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

    public void launchAlert(Alert.AlertType alertType, String title, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

}