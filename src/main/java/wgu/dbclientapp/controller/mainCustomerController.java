package wgu.dbclientapp.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

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
 * Controller class for main Customer page.
 */
public class mainCustomerController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button appointmentButton;
    @FXML
    private Button reportsButton;


    /* Customer table Buttons */
    @FXML
    private Button deleteButton;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private Button updateBtn;

    /* Customer Table */
    @FXML
    private TableView<Customer> tableCustomer;

    @FXML
    private TableColumn<Customer, String> tableCustomer_address;

    @FXML
    private TableColumn<Customer, Integer> tableCustomer_id;

    @FXML
    private TableColumn<Customer, String> tableCustomer_name;

    @FXML
    private TableColumn<Customer, String> tableCustomer_phone;

    @FXML
    private TableColumn<Customer, String> tableCustomer_postal;

    @FXML
    private Label reminderLabel;

    private static Customer selectedCustomer;
    public static Customer selectedCustomer() { return selectedCustomer; };
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    Alert alert= new Alert(Alert.AlertType.ERROR);

    /**
     * Warning Alert
     * @param title - Title of Warning Alert
     * @param header - Header of Warning Alert
     * @param content - Main body of Warning Alert
     */
    void warning(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Method for appointment reminder label.
     * The label sets a message if there's an appointment within 15mins of current time.
     */
    void reminderAppointment() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment: AppointmentList.getAllAppointment()) {

            if ( LocalDate.parse(appointment.getStartDate().split(" ")[0]).compareTo(LocalDate.parse(dtf.format(now).split(" ")[0])) == 0) {
                String startDate = appointment.getStartDate();
                String systemTime = dtf.format(now).toString();
                if (Integer.parseInt(startDate.substring(11,13)) == Integer.parseInt(systemTime.substring(11,13))) {
                    int timeDifference = Integer.parseInt(startDate.substring(14,16)) - Integer.parseInt(systemTime.substring(14,16));
                   if (timeDifference < 16 && timeDifference >= 0) {
                        reminderLabel.setText("Reminder: There's an upcoming appointment. Appointment ID: " + appointment.getAppointmentId() +
                                                ", Date: " + dtf.format(now).split(" ")[0] +
                                                ", Time: " + appointment.getStartDate().split(" ")[1]);
                   }
               }
            }
            else {
                reminderLabel.setText("Reminder: There are not upcoming appointment.");
            }
        }
    }

    /**
     * Event Handler for the add customer button.
     * Brings up the add customer page.
     * @param event Click event.
     */
    @FXML
    void addCustomer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/addCustomer.fxml"));
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
     * Event handler for the update button.
     * Brings user to the Update Customer Page
     * @param event click event
     */
    @FXML
    void updateCustomer(ActionEvent event) {
        selectedCustomer = tableCustomer.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/updateCustomer.fxml"));
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
     * Event Handler for the delete button
     * deletes the currently selected user.
     * @param event click event.
     * @throws SQLException exception for SQL handling,.
     */
    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException {
        Customer selectedCustomer = tableCustomer.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            warning("Warning", "Nothing is selected", "Please select a customer to delete.");
        } else {
            confirmationAlert.setTitle("");
            confirmationAlert.setHeaderText("Delete");
            confirmationAlert.setContentText("Are you sure you want to delete this?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                if (!selectedCustomer.getAssocAppointment().isEmpty()) {
                    warning("Delete", "Cannot be deleted", "Please delete the associated appointment before deleting the customer.");
                } else {
                    int custID = selectedCustomer.getId();
                    System.out.println(custID);
                    CustomerQuery.delete(custID);
                    CustomerList.deleteCustomer(selectedCustomer);
                    informationAlert.setContentText("Customer has been deleted");
                    informationAlert.show();
                }
            } else {
                return;
            }
        }
    }

    /**
     * Event Handler to the appointment page button
     * Brings user to the main appointment page.
     * @param event click event.
     * @throws SQLException exception for SQL handling.
     */
    @FXML
    void toAppointmentPage(ActionEvent event) throws SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/wgu/dbclientapp/mainAppointment.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        appointmentButton.getScene().getWindow().hide();

    }

    /**
     * Event Handler to the report page button
     * Brings user to the report page.
     * @param event click event.
     * @throws SQLException exception for SQL handling.
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
     * Initialize method on page initialization.
     * Populates the table with the customer from the database.
     * @param url url
     * @param resourceBundle resourecbundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        reminderAppointment();

        tableCustomer.setItems(CustomerList.getAllCustomer());
        tableCustomer_id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        tableCustomer_name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        tableCustomer_address.setCellValueFactory((new PropertyValueFactory<Customer, String>("fullAddress")));
        tableCustomer_postal.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        tableCustomer_phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));


}}
