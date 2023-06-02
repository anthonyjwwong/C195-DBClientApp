package wgu.dbclientapp.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import wgu.dbclientapp.model.*;
import wgu.dbclientapp.sqlQuery.AppointmentQuery;
import wgu.dbclientapp.sqlQuery.ContactQuery;

/**
 * Add Appointment Page Controller Class
 */
public class addAppointmentController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private ComboBox<String> aptContactDropBox;

    @FXML
    private TextField aptCustIdTextField;

    @FXML
    private TextArea aptDescriptionTextArea;


    @FXML
    private ComboBox<String> aptEndHour;

    @FXML
    private ComboBox<String> aptEndMinute;


    @FXML
    private TextField aptIDtextfield;

    @FXML
    private TextField aptLocaTextField;

    @FXML
    private DatePicker aptEndDate;

    @FXML
    private DatePicker aptStartDate;

    @FXML
    private ComboBox<String> aptStartHour;

    @FXML
    private ComboBox<String> aptStartMinute;



    @FXML
    private TextField aptTitleTextField;

    @FXML
    private TextField aptTypeTextField;

    @FXML
    private TextField aptUserIdTextField;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    Alert alert= new Alert(Alert.AlertType.ERROR);

    void warning( String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    ObservableList<String> hours = FXCollections.observableArrayList(
            "01","02","03","04","05","06","07","08","09","10","11","12",
            "13","14","15","16","17","18","19","20","21","22","23"
    );
    ObservableList<String> timeIncrement = FXCollections.observableArrayList(
            "00","10","15","20","25","30","35","40","45","50","55"
    );


    /**
     * Empty Text Field Class - Check to see if any of the input fields are blank.
     */
    void emptyTxtfield() {
        if (aptTitleTextField.getText().isBlank() || aptLocaTextField.getText().isBlank() || aptContactDropBox.getSelectionModel().isEmpty()||
        aptTypeTextField.getText().isBlank() || aptDescriptionTextArea.getText().isBlank() || aptUserIdTextField.getText().isBlank() ||
        aptCustIdTextField.getText().isBlank() || aptStartDate.getValue() == null || aptEndDate.getValue() == null ||
        aptStartHour.getSelectionModel().isEmpty() || aptStartMinute.getSelectionModel().isEmpty() ||
        aptEndHour.getSelectionModel().isEmpty() || aptEndMinute.getSelectionModel().isEmpty()) {
            warning("Empty Field", "Empty Field", "Please do not leave any of the fields empty");
        }
    }


    /**
     * Add Appointment - Event fires off when the Add button is clicked.
     * Adds all the user input into different classes and updates the Table View.
     * @param event event
     * @throws SQLException exception for the SQL handling
     */
    @FXML
    void addAppointment(ActionEvent event) throws SQLException {
        emptyTxtfield();
        /* This gets the values of all the variables to be converted to UTC */
        LocalDate startApptDate = aptStartDate.getValue();
        int startYear = startApptDate.getYear();
        int startMonth = startApptDate.getMonthValue();
        int startDay = startApptDate.getDayOfMonth();
        int startHour = Integer.parseInt(aptStartHour.getSelectionModel().getSelectedItem());
        int startMin = Integer.parseInt(aptStartMinute.getSelectionModel().getSelectedItem());

        LocalDate endApptDate = aptEndDate.getValue();
        int endYear = endApptDate.getYear();
        int endMonth = endApptDate.getMonthValue();
        int endDay = endApptDate.getDayOfMonth();
        int endHour = Integer.parseInt(aptEndHour.getSelectionModel().getSelectedItem());
        int endMin = Integer.parseInt(aptEndMinute.getSelectionModel().getSelectedItem());

        /* Variables to be added into the classes */
        int appointmentId = AppointmentList.getAllAppointment().size()+1;
        String title = aptTitleTextField.getText();
        String location = aptLocaTextField.getText();
        String contact = aptContactDropBox.getSelectionModel().getSelectedItem().toString();
        String type = aptTypeTextField.getText();
        String description = aptDescriptionTextArea.getText();
        int userId = Integer.parseInt(aptUserIdTextField.getText());
        int customerId = Integer.parseInt(aptCustIdTextField.getText());
        String startDate = String.valueOf(aptStartDate.getValue());
        String endDate = String.valueOf(aptEndDate.getValue());

        String startTime = aptStartHour.getSelectionModel().getSelectedItem().toString() + ":" + aptStartMinute.getSelectionModel().getSelectedItem().toString()
                + ":00";
        String endTime = aptEndHour.getSelectionModel().getSelectedItem().toString() + ":" + aptEndMinute.getSelectionModel().getSelectedItem().toString()
                + ":00";
        String start = startDate + " " + startTime;
        String end = endDate + " " + endTime;
        String currentTime = dtf.format(now);
        String currentUser = String.valueOf(loginController.getCurrentUser().getUsername());
        int contactId = ContactQuery.getContactId(contact);
        String contactName = ContactQuery.getContact(contactId);
        Customer newCustomer = CustomerList.getCustomer(customerId);

        /* Converts from localtime to UTC to be inserted into database */
        String utcStart = TimeConversion.localToUTC(startYear, startMonth, startDay, startHour, startMin);
        String utcEnd = TimeConversion.localToUTC(endYear, endMonth, endDay,endHour,endMin);


        /* Converts from LocalTime to EST for the input validation office hour */
        int estStartHour = TimeConversion.localToEST(startYear, startMonth, startDay, startHour, startMin);
        int estEndHour = TimeConversion.localToEST(endYear, endMonth, endDay,endHour,endMin);

        // All the input validations.
        if (estStartHour < 8 || estEndHour >= 22) {
            System.out.println(estStartHour);
            warning("Cannot Schedule Appointment", "Exceeded Office hours(EST)", "Please choose a time within normal office hours(EST)");
            return;
        }

        if (startHour > endHour) {
            warning("Cannot Schedule Appointment", "The end hour cannot be earlier than the start hour", "Please change the time.");
            return;
        }
        if (LocalDate.parse(startDate).compareTo(LocalDate.parse(endDate)) > 0 ) {
            warning("Cannot Schedule Appointment", "The start date cannot exceed the end date", "Please change the date.");
            return;
        }

        /* Test for any time overlaps */
        for (Appointment appointment: AppointmentList.getAllAppointment()) {

            if (appointment.getCustomerId() == customerId) {
                if (LocalDate.parse(appointment.getStartDate().split(" ")[0]).compareTo(LocalDate.parse(startDate.split(" ")[0])) == 0) {
                    int existStartHour = Integer.parseInt(appointment.getStartDate().substring(11,13));
                    int existStartMin = Integer.parseInt(appointment.getStartDate().substring(14,16));
                    int existEndHour = Integer.parseInt(appointment.getEndDate().substring(11,13));
                    int existEndMin = Integer.parseInt(appointment.getEndDate().substring(14,16));
                    LocalTime startTime1 = LocalTime.of( existStartHour, existStartMin );
                    LocalTime stopTime1= LocalTime.of( existEndHour , existEndMin );
                    LocalTime startTime2 = LocalTime.of(startHour  , startMin);
                    LocalTime stopTime2 = LocalTime.of( endHour , endMin );

                    Boolean isOverLap = ((startTime1.isBefore( stopTime2 )) && ( stopTime1.isAfter( startTime2 )));
                    if (isOverLap) {
                        warning("Cannot Schedule appointment", "Customer has overlapping appointments", "Please schedule another time for this customer.");
                        return;
                    }

                }
            }

            if (appointment.getAppointmentId() == appointmentId) {
                appointmentId = appointmentId + 1;
            }

        }


            /* Adding all the information into the database, appointment class and appointment list class */
            AppointmentQuery.insert(appointmentId, title, description, location, type, utcStart, utcEnd,
                    currentTime, currentUser, currentTime, currentUser, customerId, userId, contactId);


            Appointment newAppointment = new Appointment(appointmentId, title, description, location, type,
                    start, end, currentTime, currentUser, currentTime, currentUser, customerId, userId, contactId);

            newAppointment.setContactName(contactName);
            newCustomer.addAssociatedAppointment(newAppointment);

            AppointmentList.addAppointment(newAppointment);

            addAppointmentButton.getScene().getWindow().hide();

    }

    /**
     * Initialize method for when the page starts up.
     * Drop boxes are populated with the items.
     * @param url url
     * @param resourceBundle resourcebundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            aptContactDropBox.setItems(ContactQuery.getAllContact());
            aptStartHour.setItems(hours);
            aptEndHour.setItems(hours);
            aptStartMinute.setItems(timeIncrement);
            aptEndMinute.setItems(timeIncrement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


