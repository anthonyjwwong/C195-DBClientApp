package wgu.dbclientapp.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu.dbclientapp.model.Appointment;
import wgu.dbclientapp.model.AppointmentList;
import wgu.dbclientapp.sqlQuery.AppointmentQuery;
import wgu.dbclientapp.sqlQuery.ContactQuery;

/**
 * Controller for the report page
 */
public class reportsController implements Initializable {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button customerButton;

    @FXML
    private Button appointmentButton;

    @FXML
    private TableColumn<Appointment, String> lDate;

    @FXML
    private TableColumn<Appointment, String> lLocation;

    @FXML
    private TableView<Appointment> locationTable;

    @FXML
    private TableColumn<Appointment, String> cContact;

    @FXML
    private TableColumn<Appointment, Integer> cCustomerId;

    @FXML
    private TableColumn<Appointment, String> cDescription;

    @FXML
    private TableColumn<Appointment, String> cEnd;

    @FXML
    private TableColumn<Appointment, Integer> cId;

    @FXML
    private TableColumn<Appointment, String> cLocation;

    @FXML
    private TableColumn<Appointment, String> cStart;

    @FXML
    private TableColumn<Appointment, String> cTitle;

    @FXML
    private TableColumn<Appointment, String> cType;

    @FXML
    private TableColumn<Appointment, Integer> cUserId;

    @FXML
    private TableView<Appointment> contactTable;

    @FXML
    private ComboBox<String> dbContact;

    @FXML
    private ComboBox<String> dbMonth;

    @FXML
    private TableColumn<Appointment, String> mMonth;

    @FXML
    private TableColumn<Appointment, Integer> mNumOfAppoints;

    @FXML
    private TableColumn<Appointment, String> mType;

    @FXML
    private TableView<newReport> monthTable;

    ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June", "July","August","September"
            ,"October","November","December"
    );

    ObservableList<String> contacts = FXCollections.observableArrayList(
            "Anika Costa", "Daniel Garcia", "Li Lee"
    );

    /**
     * New Report Class for the month/type/appointment report.
     */
    public class newReport {

        private String month;
        private String type;
        private Integer numOfAppoint;

        /**
         * Constructor for the new report class.
         * @param month String month
         * @param type String type
         * @param numOfAppoint int NumofAppoint
         */
        public newReport(String month, String type, int numOfAppoint) {
            this.month = month;
            this.type = type;
            this.numOfAppoint = numOfAppoint;
        }

        /**
         * Getter for month
         * @return month
         */
        public String getMonth() {
            return month;
        }

        /**
         * Setter for month
         * @param month set the month
         */
        public void setMonth(String month) {
            this.month = month;
        }

        /**
         * Getter for Type
         * @return type
         */
        public String getType() {
            return type;
        }

        /**
         * Setter for type
         * @param type String type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Getter for numofappoint
         * @return numofappoint
         */
        public int getNumOfAppoint() {
            return numOfAppoint;
        }

        /**
         * Set num of appoint
         * @param numOfAppoint int numofapoint.
         */
        public void setNumOfAppoint(int numOfAppoint) {
            this.numOfAppoint = numOfAppoint;
        }

        /**
         * Equal method for comparison
         * @param obj
         * @return true/false
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof newReport) {
                newReport temp = (newReport) obj;
                if (this.month.equals(temp.month) && this.type.equals(temp.type) && this.numOfAppoint.equals(temp.numOfAppoint))
                    return true;
                }
                return false;

        }


        /**
         * HashCode method for comparing
         * @return the hashcodes.
         */
        @Override
        public int hashCode() {
            return (this.month.hashCode() + this.type.hashCode() + this.numOfAppoint.hashCode());
        }
    }

    /**
     * Event Handling for the month drop box.
     * Depending on the month selected, the month - type - numofappointment will be shown for that month
     *
     * Lambda expression was used in the forEach loop for the appointmentlist to get the information.
     * Improves the Readability of the loop.
     *
     * @param event selected event
     * @throws SQLException exception for SQL handling
     */
    @FXML
    void selectedMonth(ActionEvent event) throws SQLException {
        String month = dbMonth.getSelectionModel().getSelectedItem().toString();
        int monthIndex = dbMonth.getSelectionModel().getSelectedIndex();
        ObservableList<newReport> newReportList = FXCollections.observableArrayList();
        Set<newReport> noDuplicates = new HashSet<>();
        ObservableList<newReport> finalNewReportList = newReportList;

        AppointmentList.getAllAppointment().forEach((appointment) -> {
            if (monthIndex == (Integer.parseInt(appointment.getStartDate().substring(5,7))-1)) {
                int numoftype = 0;
                try {
                    numoftype = AppointmentQuery.countType(appointment.getType());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newReport report = new newReport(month, appointment.getType(), numoftype);
                finalNewReportList.add(report);
            }
        });

        noDuplicates.addAll(finalNewReportList);
        newReportList = FXCollections.observableArrayList();
        newReportList.addAll(noDuplicates);

        monthTable.setItems(newReportList);
        mMonth.setCellValueFactory(new PropertyValueFactory<Appointment, String>("month"));
        mType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        mNumOfAppoints.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("numOfAppoint"));

    }
    /**
     * Event Handling for the contact drop box
     * Depending on the contact selected, shows all the information on the table for that selected contact.
     *
     * Lambda expression was used in the forEach loop for the appointmentlist to look for the information.
     * Improves the Readability of the loop.
     *
     * @param event selected Event
     * @throws SQLException exception for SQL handling.
     */
    @FXML
    void selectedContact(ActionEvent event) throws SQLException {
        String contact = dbContact.getSelectionModel().getSelectedItem().toString();
        ObservableList<Appointment> newContactList = FXCollections.observableArrayList();

        AppointmentList.getAllAppointment().forEach((appointment -> {
            if(appointment.getContactName().contains(contact)) {
                Appointment newAppointment = new Appointment(appointment.getAppointmentId(), appointment.getTitle(),
                        appointment.getDescription(), appointment.getLocation(),
                        appointment.getType(), appointment.getStartDate(), appointment.getEndDate(), appointment.getCustomerId(),
                        appointment.getUserId(),appointment.getContactId());

                try {
                    newAppointment.setContactName(ContactQuery.getContact(appointment.getContactId()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newContactList.add(newAppointment);
            }
        }));

        contactTable.setItems(newContactList);
        cId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        cTitle.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
        cDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        cLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        cContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        cType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        cStart.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
        cEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endDate"));
        cCustomerId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        cUserId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
    }

    /**
     * Customer page button Event Handler
     * Brings user to the customer page on click.
     * @param event click event
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
     * Appointment page button Event Handler
     * Brings user to the appointment page on click.
     * @param event click event
     */
    @FXML
    void toAppointmentPage(ActionEvent event) {
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
     * Function to set the information on the third table, location table.
     * The table shows the location and date.
     */
    void setLocationTable() {
        ObservableList<Appointment> newLocationList = FXCollections.observableArrayList();
        for (Appointment appointment: AppointmentList.getAllAppointment()) {
            String location = appointment.getLocation();
            String startDate = appointment.getStartDate();

            Appointment appointments = new Appointment(location, startDate);
            newLocationList.add(appointments);

        }

        locationTable.setItems(newLocationList);
        lLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        lDate.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
    }

    /**
     * On page initialize, the dropboxes are filled, and location table is populated.
     * @param url url
     * @param resourceBundle resourcebundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLocationTable();

        dbMonth.setItems(months);
        dbContact.setItems(contacts);
    }
}
