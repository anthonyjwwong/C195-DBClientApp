package wgu.dbclientapp.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu.dbclientapp.model.Appointment;
import wgu.dbclientapp.model.AppointmentList;
import wgu.dbclientapp.model.Customer;
import wgu.dbclientapp.model.CustomerList;
import wgu.dbclientapp.sqlQuery.AppointmentQuery;
import wgu.dbclientapp.sqlQuery.CustomerQuery;

/**
 * Main Appointment Controller - where the tables and main page for appointments are.
 */
public class mainAppointmentController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private RadioButton toggleAllView;

    @FXML
    private ToggleGroup toggleApptView;

    @FXML
    private RadioButton toggleMonthView;

    @FXML
    private RadioButton toggleWeekView;

    @FXML
    private TableView<Appointment> tableAppt;

    @FXML
    private TableColumn<Appointment, String> aptContacts;

    @FXML
    private TableColumn<Appointment, Integer> aptCustomerId;

    @FXML
    private TableColumn<Appointment, String> aptDescription;

    @FXML
    private TableColumn<Appointment, String> aptEnd;

    @FXML
    private TableColumn<Appointment, Integer> aptID;

    @FXML
    private TableColumn<Appointment, String> aptLocation;

    @FXML
    private TableColumn<Appointment, String> aptStart;

    @FXML
    private TableColumn<Appointment, String> aptTitle;

    @FXML
    private TableColumn<Appointment, String> aptType;

    @FXML
    private TableColumn<Appointment, Integer> aptUserId;

    @FXML
    private Button deleteApptButton;

    @FXML
    private Button addAptButton;

    @FXML
    private Button updateApptButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button customerButton;
    LocalDate currentdate = LocalDate.now();
    public static Appointment selectedAppointment;
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    Alert alert= new Alert(Alert.AlertType.ERROR);

    /**
     * Warning Alert
     * @param title - Title of Alert
     * @param header Header of Alert
     * @param content Main body of Alert
     */
    void warning(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Access to the current selectedAppointment class
     * @return selectedAppointment
     */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }


    /**
     * Event Handler for the Customer Button.
     * @param event click event.
     */
    @FXML
    void toCustomerPage(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/mainCustomer.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        customerButton.getScene().getWindow().hide();
    }

    /**
     * Event Handler for the add appointment button
     * Moves the user to the addappointment page onclick.
     * @param event Click handler
     */
    @FXML
    void addAppointment(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/addAppointment.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    void updateAppointment(ActionEvent event) {
        selectedAppointment = tableAppt.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/updateAppointment.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * Event handler for the delete button.
     * Deletes the currently selected appointment onclick.
     * @param event event
     * @throws SQLException sqlException for the SQL Handling.
     */
    @FXML
    void deleteAppointment(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = tableAppt.getSelectionModel().getSelectedItem();
        Customer currentCustomer = CustomerList.getCustomer(selectedAppointment.getCustomerId());

        if (selectedAppointment == null) {
            warning("Warning", "Nothing is selected", "Please select an appointment to delete.");
        } else {
            confirmationAlert.setTitle("");
            confirmationAlert.setHeaderText("Delete");
            confirmationAlert.setContentText("Are you sure you want to delete this?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int apptId = selectedAppointment.getAppointmentId();
                currentCustomer.removeAssociatedAppointment(selectedAppointment);
                System.out.println(apptId);
                AppointmentQuery.delete(apptId);
                AppointmentList.deleteAppointment(selectedAppointment);
                informationAlert.setContentText("Appointment ID:" + selectedAppointment.getAppointmentId()+ " \nAppointment Type: " + selectedAppointment.getType() + " \nhas been deleted.");
                informationAlert.show();
            } else {
                return;
            }
        }

    }

    /**
     * Populate Table Class -
     * Used to populate the Table depending on the different filter (All, month, Week)
     * @param appointmentListToGet The appointment list to populate the table with.
     */
    void populateTable(ObservableList<Appointment> appointmentListToGet) {
        tableAppt.setItems(appointmentListToGet);

        aptID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        aptTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        aptDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        aptLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        aptContacts.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        aptType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        aptStart.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
        aptEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endDate"));
        aptCustomerId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        aptUserId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
    }

    /**
     * The event Handler for the toggle all toggle button
     * @param event click event.
     */
    @FXML
    void toggleAll(ActionEvent event) {
        toggleAllView.setSelected(true);
        toggleMonthView.setSelected(false);
        toggleWeekView.setSelected(false);
        populateTable(AppointmentList.getAllAppointment());
    }

    /**
     * Event Handler for the toggle Month toggle Button
     * @param event click event.
     */
    @FXML
    void toggleMonth(ActionEvent event) {
        toggleAllView.setSelected(false);
        toggleMonthView.setSelected(true);
        toggleWeekView.setSelected(false);
        ObservableList<Appointment> newAppointmentList = FXCollections.observableArrayList();

        int month = currentdate.getMonthValue();
        for (Appointment appointment: AppointmentList.getAllAppointment()) {
            if (Integer.parseInt(appointment.getStartDate().substring(5,7)) == month) {
                newAppointmentList.add(appointment);
            }

        }
        tableAppt.setItems(newAppointmentList);
    }

    /**
     * Event handler for the toggle week toggle button.
     * @param event click event.
     */
    @FXML
    void toggleWeek(ActionEvent event) {
        toggleAllView.setSelected(false);
        toggleMonthView.setSelected(false);
        toggleWeekView.setSelected(true);
        ObservableList<Appointment> newAppointmentList = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        TemporalField weekofyear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNum = date.get(weekofyear);
        int weekNum2;

        for(Appointment appointment:AppointmentList.getAllAppointment()) {
            int year = Integer.parseInt(appointment.getStartDate().substring(0,4));
            int month = Integer.parseInt(appointment.getStartDate().substring(5,7));
            int day = Integer.parseInt(appointment.getStartDate().substring(8,10));
            LocalDate date2 = LocalDate.of(year, month, day);
            weekNum2 = date2.get(weekofyear);
            if (weekNum == weekNum2) {
                newAppointmentList.add(appointment);
            }
        }
        tableAppt.setItems(newAppointmentList);

    }

    /**
     * Event Handler for the report Button
     * Moves user to the report page
     * @param event click event
     *
     */
    @FXML
    void toReportPage(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/reports.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        reportsButton.getScene().getWindow().hide();
    }

    /**
     * Method that runs on page initialize.
     * Populates the table, and set toggle All button to true.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleAllView.setSelected(true);
        populateTable(AppointmentList.getAllAppointment());
    }
}
